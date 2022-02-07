package com.graduation.parrot.controller;

import com.graduation.parrot.domain.form.UserSaveForm;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "security/login";
    }

    @GetMapping("/signup")
    public String signupView(){
        return "security/signup";
    }

    @PostMapping("/signup")
    public String signup(UserSaveForm userSaveForm){
        userService.save(userSaveForm);
        return "redirect:/";
    }
}
