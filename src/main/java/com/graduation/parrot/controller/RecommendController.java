package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.dto.RecommendDto;
import com.graduation.parrot.service.CommentService;
import com.graduation.parrot.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.graduation.parrot.service.RecommendServiceImpl.HATE;
import static com.graduation.parrot.service.RecommendServiceImpl.LIKE;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RecommendController {
    private final RecommendService recommendService;
    private final CommentService commentService;

    @PostMapping("/like/{board_id}")
    public RecommendDto like(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.recommend(principalDetails.getUser(), board_id, LIKE);
    }

    @PostMapping("/hate/{board_id}")
    public RecommendDto hate(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.recommend(principalDetails.getUser(), board_id, HATE);
    }

    @PostMapping("/recommendcheck/{board_id}")
    public Map<String, String> recommendCheck(@PathVariable Long board_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return recommendService.alreadyRecommendCheck(principalDetails.getUser(), board_id);
    }

    @PostMapping("/comment/like/{comment_id}")
    public RecommendDto commentLike(@PathVariable Long comment_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return commentService.recommend(principalDetails.getUser(), comment_id, LIKE);
    }

    @PostMapping("/comment/hate/{comment_id}")
    public RecommendDto commentHate(@PathVariable Long comment_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        return commentService.recommend(principalDetails.getUser(), comment_id, HATE);
    }

    @PostMapping("/comment/recommendcheck/{comment_id}")
    public Map<String, String> commentRecommendCheck(@PathVariable Long comment_id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        log.info("commentRecommendCheck");
        return commentService.recommendCheck(principalDetails.getUser(), comment_id);
    }
}
