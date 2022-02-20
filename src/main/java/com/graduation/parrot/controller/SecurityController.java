package com.graduation.parrot.controller;

import com.graduation.parrot.domain.form.UserSaveForm;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;

    @GetMapping("/userlogin")
    public String loginView() {
        return "security/login";
    }

    @PostMapping("/userlogout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "jwtToken");
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/signup")
    public String signupView() {
        return "security/signup";
    }

    @PostMapping("/signup")
    public String signup(UserSaveForm userSaveForm) {
        userService.save(userSaveForm);
        return "redirect:/";
    }
}
