package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;
import com.graduation.parrot.domain.dto.UserResponseDto;
import com.graduation.parrot.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping({"/", "/board"})
    public String index(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 20) Pageable pageable) {
        if (principalDetails != null) {
            model.addAttribute("userName", principalDetails.getUser().getName());
        }
        model.addAttribute("boardList", boardService.getBoardList(pageable));
        return "index";
    }

    @GetMapping("/boardlist")
    public String boardList() {
        return "board/boardList";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        UserResponseDto userResponseDto = new UserResponseDto(user);
        BoardResponseDto boardResponseDto = boardService.getBoard(id);

        model.addAttribute("userResponseDto", userResponseDto);
        model.addAttribute("boardResponseDto", boardResponseDto);
        return "board/board";
    }

    @GetMapping("/board/insert")
    public String createBoardView() {
        return "board/createBoard";
    }

    @PostMapping("/board/insert")
    public String createBoard(BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        boardService.create(boardDto, principalDetails.getUser());
        return "redirect:/";
    }

    @GetMapping("/board/update/{id}")
    public String updateBoardView(@PathVariable Long id, Model model) {
        model.addAttribute("boardResponseDto", boardService.getBoard(id));
        return "board/updateBoard";
    }

    @PutMapping("/board/update/{id}")
    public String updateBoard(@PathVariable Long id, BoardDto boardDto) {
        boardService.update(id, boardDto);
        return "redirect:/";
    }

    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/";
    }


}
