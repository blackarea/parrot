package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;

import java.util.List;

public interface BoardService {
    Long insert(BoardDto boardDto, User user);
    Long update(Long board_id, BoardDto boardDto);
    void delete(Long board_id);
    BoardResponseDto getBoard(Long board_id);
    List<BoardListResponseDto> getBoardList();
}
