package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.dto.*;
import com.graduation.parrot.service.UserService;
import com.graduation.parrot.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserService userService;
    private final SignUpValidator signUpValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(signUpValidator);
    }

    @GetMapping("/userlogin")
    public String loginView() {
        return "security/login";
    }

    @PostMapping("/userlogout")
    public String logout(HttpServletResponse response) {
        expireCookie(response);
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signupView() {
        return "security/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserSaveDto userSaveDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("userSaveDto", userSaveDto);

            // 유효성 통과 못한 필드와 메시지를 핸들링
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "security/signup";
        }/*
        if (userSaveDto.getEmail().equals("")) {
            userSaveDto.setEmail(null);
        }*/
        userService.create(userSaveDto);
        return "redirect:/";
    }

    @GetMapping("/account/activity/{login_id}")
    public String activity(@PathVariable String login_id, Model model, @PageableDefault(size = 10) Pageable pageable,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        model.addAttribute("userName", principalDetails.getUser().getName());
        model.addAttribute("userActivityListDto", userService.getUserActivityPaging(login_id, pageable));
        return "security/activity";
    }

    @GetMapping("/account/{login_id}")
    public String account(@PathVariable String login_id, Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserResponseDto userResponseDto = new UserResponseDto(principalDetails.getUser());
        model.addAttribute("userResponseDto", userResponseDto);
        return "security/account";
    }

    @PutMapping("/account/password/{login_id}")
    public void updatePassword(@PathVariable String login_id, @RequestBody Map<String, String> name) {
        userService.updatePassword(login_id, name.get("password"));
    }

    @ResponseBody
    @PutMapping("/account/{login_id}")
    public void updateAccount(@PathVariable String login_id, @RequestBody UserUpdateDto userUpdateDto, HttpServletResponse response) {
        userService.updateName(login_id, userUpdateDto.getUsername());
        userService.updateEmail(login_id, userUpdateDto.getEmail());
    }

    private void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
