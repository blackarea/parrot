package com.graduation.parrot.controller;

import com.graduation.parrot.domain.form.BoardForm;
import com.graduation.parrot.domain.form.BoardSaveForm;
import com.graduation.parrot.repository.UserRepository;
import com.graduation.parrot.service.BoardService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/board/insert")
    public String insertBoardView() {
        return "/board/insertBoard";
    }

    @PostMapping("/board/insert")
    public String insertBoard(BoardForm boardForm) {
        boardService.insert(boardForm, userRepository.findById(1L).get());
        return "redirect:/";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model) {
        model.addAttribute("boardResponseForm", boardService.getBoard(id));
        return "/board/getBoard";
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
