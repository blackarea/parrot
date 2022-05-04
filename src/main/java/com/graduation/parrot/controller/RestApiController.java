package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.ParrotStateDto;
import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.exception.ApiException;
import com.graduation.parrot.exception.ExceptionEnum;
import com.graduation.parrot.repository.ParrotStateRepository;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class RestApiController {
    private final UserService userService;

    @PostMapping("/exception")
    public void exceptionTest() throws ApiException {
        throw new ApiException(ExceptionEnum.ALREADY_EXIST_USERNAME);
    }

    @PostMapping("/signup")
    public String join(@RequestBody @Valid UserSaveDto userSaveDto) {
        userService.create(userSaveDto);
        return "회원가입완료";
    }

    @GetMapping("/state/{login_id}")
    public ParrotStateDto sendState(@PathVariable String login_id) {
        return userService.getParrotState(login_id);
    }

    @PostMapping("/state/{login_id}")
    public void receiveAndSetState(@PathVariable String login_id, @RequestBody ParrotStateDto parrotStateDto) {
        userService.setParrotState(login_id, parrotStateDto);
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
}
