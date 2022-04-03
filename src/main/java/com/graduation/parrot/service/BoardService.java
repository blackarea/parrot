package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Long create(BoardDto boardDto, User user);
    Long update(Long board_id, BoardDto boardDto);
    void delete(Long board_id);
    BoardResponseDto getBoard(Long board_id);
    Page<BoardListResponseDto> getBoardList(Pageable pageable);
    Page<BoardListResponseDto> getBoardListPagingSearch(Pageable pageable, String type, String searchKeyword);
    int updateView(Long board_id);
}
