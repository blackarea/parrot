package com.graduation.parrot.controller;

import com.graduation.parrot.domain.ChatData;
import com.graduation.parrot.domain.dto.ChatDataDto;
import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.ParrotDataDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.exception.ApiException;
import com.graduation.parrot.exception.ExceptionEnum;
import com.graduation.parrot.service.ChatDataService;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class RestApiController {
    private final UserService userService;
    private final ChatDataService chatDataService;

    @PostMapping("/exception")
    public void exceptionTest() throws ApiException {
        throw new ApiException(ExceptionEnum.ALREADY_EXIST_USERNAME);
    }

    @PostMapping("/signup")
    public String join(@RequestBody @Valid UserSaveDto userSaveDto) {
        userService.create(userSaveDto);
        return "회원가입완료";
    }

    @GetMapping("/duplicate/{duplicated_id}")
    public Map<String, String> validateDuplicateId(@PathVariable String duplicated_id) {
        Map<String, String> map = new HashMap<>();
        if (userService.validateDuplicateUser(duplicated_id)) {
            map.put("duplicate", "yes");
        } else {
            map.put("duplicate", "no");
        }
        return map;
    }

    @GetMapping("/duplicate/name/{username}")
    public Map<String, String> validateDuplicateName(@PathVariable String username) {
        Map<String, String> map = new HashMap<>();
        if (userService.validateDuplicateUsername(username)) {
            map.put("duplicate", "yes");
        } else {
            map.put("duplicate", "no");
        }
        return map;
    }

    @PutMapping("/user/name/{login_id}")
    public void changeName(@PathVariable String login_id, @RequestBody Map<String, String> name) {
        userService.updateName(login_id, name.get("name"));
    }

    @GetMapping("/state/{login_id}")
    public ParrotStateDto sendState(@PathVariable String login_id) {
        return userService.getParrotState(login_id);
    }

    @PostMapping("/state/{login_id}")
    public void receiveAndSetState(@PathVariable String login_id, @RequestBody ParrotStateDto parrotStateDto) {
        userService.setParrotState(login_id, parrotStateDto);
    }

    @GetMapping("/parrotdata/{login_id}/{page}")
    public ResponseEntity<ParrotDataDto> sendParrotData(@PathVariable String login_id, @PathVariable int page) {
        Optional<ParrotDataDto> parrotDataDto = userService.getParrotData(login_id, page);
        if (parrotDataDto.isPresent()) {
            return ResponseEntity.ok().body(parrotDataDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/parrotdata/{login_id}/{page}")
    public void receiveAndSetParrotData(@PathVariable String login_id, @PathVariable int page,
                                        @RequestBody ParrotDataDto parrotDataDto) {
        userService.setParrotData(login_id, parrotDataDto);
    }

    @GetMapping("/parrotdata/pagesize/{login_id}")
    public Map<String, Integer> sendPageSize(@PathVariable String login_id) {
        Map<String, Integer> map = new HashMap<>();
        map.put("pageSize", userService.getParrotDataPageSize(login_id));
        return map;
    }

    @GetMapping("/chatdata/{login_id}")
    public List<ChatData> getAllChatData(@PathVariable String login_id) {
        List<ChatData> allChatData = chatDataService.getAllChatData(login_id);
        return allChatData;
    }

    @PostMapping("/chatdata/{login_id}")
    public Map<String, Long> createChatData(@PathVariable String login_id, @RequestBody ChatDataDto chatDataDto) {
        Map<String, Long> map = new HashMap<>();
        map.put("id", chatDataService.create(login_id, chatDataDto));
        return map;
    }

    @PutMapping("/chatdata/{login_id}/{chatdata_id}")
    public void updateChatData(@PathVariable String login_id, @PathVariable Long chatdata_id,
                               @RequestBody Map<String, Integer> radio) {
        chatDataService.updateChatData(login_id, chatdata_id, radio.get("radio"));
    }

    @DeleteMapping("/chatdata/{login_id}/{chatdata_id}")
    public void deleteChatData(@PathVariable String login_id, @PathVariable Long chatdata_id) {
        chatDataService.deleteChatData(login_id, chatdata_id);
    }

}
