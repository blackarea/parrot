package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.dto.User.UserActivityPageDto;
import com.graduation.parrot.domain.dto.User.UserResponseDto;
import com.graduation.parrot.domain.dto.User.UserTeachingListDto;
import com.graduation.parrot.domain.dto.User.UserUpdateDto;
import com.graduation.parrot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account/{login_id}")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public String account(@PathVariable String login_id, Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserResponseDto userResponseDto = new UserResponseDto(principalDetails.getUser());
        model.addAttribute("userResponseDto", userResponseDto);
        return "security/account";
    }

    @GetMapping("/activity")
    public String activity(@PathVariable String login_id, Model model, @PageableDefault(size = 8) Pageable pageable,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserActivityPageDto userActivityPaging = userService.getUserActivityPaging(login_id, pageable);
        int boardCount = userActivityPaging.getBoardCount();
        int commentCount = userActivityPaging.getCommentCount();

        model.addAttribute("login_Id", login_id);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("userName", principalDetails.getUser().getName());
        model.addAttribute("userActivityListDto", userActivityPaging.getPage());
        return "security/activity";
    }

    @GetMapping("/teaching")
    public String teachingList(@PathVariable String login_id, Model model){
        List<UserTeachingListDto> userTeachingList = userService.getUserTeachingList(login_id);
        model.addAttribute("userTeachingList", userTeachingList);
        return "security/teaching";
    }

    @ResponseBody
    @PostMapping("/teaching")
    public String teaching(@PathVariable String login_id, @RequestBody Map<String, String> teachingId){
        return userService.getTeachContent(Long.valueOf(teachingId.get("teachingId")));
    }

    @ResponseBody
    @PutMapping("/password")
    public boolean updatePassword(@PathVariable String login_id, @RequestBody UserUpdateDto userUpdateDto) {
        //true 성공, false 실패
        return userService.updatePassword(login_id, userUpdateDto.getOldPassword(), userUpdateDto.getNewPassword());
    }

    @ResponseBody
    @PutMapping("/name")
    public void updateName(@PathVariable String login_id, @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateName(login_id, userUpdateDto.getUsername());
    }

    @ResponseBody
    @PutMapping("/email")
    public void updateEmail(@PathVariable String login_id, @RequestBody UserUpdateDto userUpdateDto) {
        userService.updateEmail(login_id, userUpdateDto.getEmail());
    }

}
