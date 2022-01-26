package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.BoardForm;
import com.graduation.parrot.domain.form.BoardListResponseForm;
import com.graduation.parrot.domain.form.BoardResponseForm;
import com.graduation.parrot.domain.form.BoardSaveForm;

import java.util.List;

public interface BoardService {
    Long insert(BoardForm boardForm, User user);
    Long update(Long board_id, BoardForm boardForm);
    void delete(Long board_id);
    BoardResponseForm getBoard(Long board_id);
    List<BoardListResponseForm> getBoardList();
}
