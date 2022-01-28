package com.graduation.parrot.controller;

import com.graduation.parrot.config.SecurityUser;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.BoardForm;
import com.graduation.parrot.domain.form.BoardResponseForm;
import com.graduation.parrot.domain.form.BoardSaveForm;
import com.graduation.parrot.repository.UserRepository;
import com.graduation.parrot.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserRepository userRepository;

    @GetMapping({"/", "/board"})
    public String index(Model model){

        model.addAttribute("boardList", boardService.getBoardList());
        return "index";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model, @AuthenticationPrincipal SecurityUser principal) {
        User user = principal.getUser();
        BoardResponseForm boardResponseForm = boardService.getBoard(id);
        model.addAttribute("user", user);
        model.addAttribute("boardResponseForm", boardResponseForm);
        return "/board/getBoard";
    }

    @GetMapping("/board/insert")
    public String insertBoardView() {
        return "/board/insertBoard";
    }

    @PostMapping("/board/insert")
    public String insertBoard(BoardForm boardForm, @AuthenticationPrincipal SecurityUser principal) {
        boardService.insert(boardForm, principal.getUser());
        return "redirect:/";
    }

    @GetMapping("/board/update/{id}")
    public String updateBoardView(@PathVariable Long id, Model model) {
        model.addAttribute("boardResponseForm", boardService.getBoard(id));
        return "/board/updateBoard";
    }

    @PutMapping("/board/update/{id}")
    public String updateBoard(@PathVariable Long id, BoardForm boardForm) {
        boardService.update(id, boardForm);
        return "redirect:/";
    }

    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/";
    }


}
