package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.UserSaveDto;
import com.graduation.parrot.repository.UserRepository;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;
    private final UserRepository userRepository;

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

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/admin/userlist")
    public List<User> userList() {
        return userRepository.findAll();
    }

    @PostMapping("/join")
    public String join(@RequestBody UserSaveDto userSaveDto) {
        userService.save(userSaveDto);
        return "회원가입완료";
    }
}
