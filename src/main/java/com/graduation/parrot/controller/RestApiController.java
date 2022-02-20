package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.UserSaveForm;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/token")
    public String token() {
        System.out.println("token");
        return "token";
    }

    @GetMapping("user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        System.out.println("principal : " + user.getId());
        System.out.println("principal : " + user.getLogin_id());
        System.out.println("principal : " + user.getPassword());

        return "user";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/join")
    public String join(@RequestBody UserSaveForm userSaveForm) {
        userService.save(userSaveForm);
        return "회원가입완료";
    }
}
