package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecommendController {
    private final RecommendService recommendService;

    @PostMapping("/like/{board_id}")
    public boolean like(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.like(principalDetails.getUser(), board_id);
    }

    @PostMapping("/hate/{board_id}")
    public boolean hate(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.hate(principalDetails.getUser(), board_id);
    }
}
