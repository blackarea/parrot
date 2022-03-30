package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
        List<CommentResponseDto> commentList = commentService.getCommentList(board_id);
        return commentList;
    }

    @PostMapping()
    public void createComment(@PathVariable Long board_id, @RequestBody Map<String, String> content,
                              @AuthenticationPrincipal PrincipalDetails principalDetails){
        commentService.create(principalDetails.getUser(), board_id, content.get("chat"));
    }

    @PutMapping("/{comment_id}")
    public void updateComment(@PathVariable String board_id, @PathVariable Long comment_id, @RequestBody String content){
        commentService.update(comment_id, content);
    }

    @DeleteMapping("/{comment_id}")
    public void deleteComment(@PathVariable String board_id, @PathVariable Long comment_id){
        commentService.delete(comment_id);
    }
}
