package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/board/{board_id}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping()
    public List<CommentResponseDto> getCommentList(@PathVariable Long board_id){
        return commentService.getCommentList(board_id);
    }

    @GetMapping("/best")
    public List<CommentResponseDto> getBestCommentList(@PathVariable Long board_id){
        return commentService.getBestCommentList(board_id);
    }

    @PostMapping()
    public void createComment(@PathVariable Long board_id, @RequestBody Map<String, String> content,
                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        commentService.create(principalDetails.getUser(), board_id, content.get("chat"));
    }

    @PutMapping("/{comment_id}")
    public void updateComment(@PathVariable String board_id, @PathVariable Long comment_id,
                              @RequestBody Map<String, String> content){
        commentService.update(comment_id, content.get("commentContent"));
    }

    @DeleteMapping("/{comment_id}")
    public void deleteComment(@PathVariable Long board_id, @PathVariable Long comment_id){
        commentService.delete(board_id, comment_id);
    }
}
