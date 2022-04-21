package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.domain.dto.User.UserResponseDto;
import com.graduation.parrot.service.BoardService;
import com.graduation.parrot.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        if (principalDetails != null) {
            model.addAttribute("login_id", principalDetails.getUser().getLogin_id());
        }
        return "index";
    }

    @GetMapping("/boardlist")
    public String boardList(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails, @PageableDefault(size = 15, direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(defaultValue = "all") String type, String searchKeyword, @RequestParam(defaultValue = "all") String array) {
        log.info(array);
        if (principalDetails != null) {
            model.addAttribute("userName", principalDetails.getUser().getName());
        }

        if (searchKeyword != null) {
            model.addAttribute("boardList", boardService.getBoardListPagingSearch(pageable, type, searchKeyword, array));
        } else if (searchKeyword == null) {
            model.addAttribute("boardList", boardService.getBoardList(pageable, array));
        }
        return "board/boardList";
    }

    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails,
                           HttpServletRequest request, HttpServletResponse response) {
        viewCountUp(id, request, response);

        User user = principalDetails.getUser();
        UserResponseDto userResponseDto = new UserResponseDto(user);
        BoardResponseDto boardResponseDto = boardService.getBoard(id);
        List<CommentResponseDto> commentDtoList = commentService.getCommentList(id);

        model.addAttribute("userResponseDto", userResponseDto);
        model.addAttribute("boardResponseDto", boardResponseDto);
        model.addAttribute("commentDtoList", commentDtoList);
        return "board/board";
    }

    @GetMapping("/board/create")
    public String createBoardView() {
        return "board/createBoard";
    }

    @PostMapping("/board")
    public String createBoard(BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long board_id = boardService.create(boardDto, principalDetails.getUser());
        return "redirect:/board/" + board_id;
    }

    @GetMapping("/board/update/{id}")
    public String updateBoardView(@PathVariable Long id, Model model) {
        model.addAttribute("boardResponseDto", boardService.getBoard(id));
        return "board/updateBoard";
    }

    @PutMapping("/board/{id}")
    public String updateBoard(@PathVariable Long id, BoardDto boardDto) {
        boardService.update(id, boardDto);
        return "redirect:/board/" + id;
    }

    @ResponseBody
    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
    }

    private void viewCountUp(Long board_id, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + board_id.toString() + "]")) {
                boardService.updateView(board_id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + board_id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(1 * 1); //1시간 //TODO 나중에 24시간으로 변경 밑에 쿠키도
                oldCookie.setHttpOnly(true);
                response.addCookie(oldCookie);
            }
        } else {
            boardService.updateView(board_id);
            Cookie newCookie = new Cookie("postView", "[" + board_id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(1 * 1); //1시간
            newCookie.setHttpOnly(true);
            response.addCookie(newCookie);
        }
    }

}
