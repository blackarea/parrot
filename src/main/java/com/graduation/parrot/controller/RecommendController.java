package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.dto.RecommendDto;
import com.graduation.parrot.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.graduation.parrot.service.RecommendServiceImpl.HATE;
import static com.graduation.parrot.service.RecommendServiceImpl.LIKE;

@RequiredArgsConstructor
@RestController
public class RecommendController {
    private final RecommendService recommendService;

    @PostMapping("/like/{board_id}")
    public RecommendDto like(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.recommend(principalDetails.getUser(), board_id, LIKE);
    }

    @PostMapping("/hate/{board_id}")
    public RecommendDto hate(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.recommend(principalDetails.getUser(), board_id, HATE);
    }
}
