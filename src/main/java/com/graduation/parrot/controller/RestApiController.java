package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.UserSaveForm;
import com.graduation.parrot.repository.UserRepository;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @PostMapping("/token")
    public String token(){
        return "token";
    }

    @GetMapping("user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : "+principal.getUser().getId());
        System.out.println("principal : "+principal.getUser().getLogin_id());
        System.out.println("principal : "+principal.getUser().getPassword());

        return "<h1>user</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "회원가입완료";
    }
}
