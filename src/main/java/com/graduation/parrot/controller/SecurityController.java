package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.User.UserSaveDto;
import com.graduation.parrot.service.MailService;
import com.graduation.parrot.service.UserService;
import com.graduation.parrot.validator.SignUpValidator;
import lombok.RequiredArgsConstructor;
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
    private final MailService mailService;
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
        }
        userService.create(userSaveDto);
        return "redirect:/";
    }

    @GetMapping("/findpassword")
    public String findPassword(){
        return "security/findPassword";
    }

    @ResponseBody
    @PostMapping("/findpassword")
    public boolean sendPasswordMail(@RequestBody Map<String, String> mail){
        return mailService.findPassword(mail.get("mail"));
    }

    @ResponseBody
    @PostMapping("/withdraw/{login_id}")
    public void withdraw(@PathVariable String login_id, HttpServletResponse response) {
        expireCookie(response);
        userService.withdraw(login_id);
    }

    private void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


}
