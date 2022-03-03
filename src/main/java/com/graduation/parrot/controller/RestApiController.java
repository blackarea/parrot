package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.exception.ApiException;
import com.graduation.parrot.exception.ExceptionEnum;
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
        userService.save(userSaveDto);
        return "회원가입완료";
    }

    @GetMapping("/duplicate/{login_id}")
    public Map<String, String> validateDuplicateId(@PathVariable String login_id) {
        Map<String, String> map = new HashMap<>();
        if (userService.validateDuplicateUser(login_id)) {
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
