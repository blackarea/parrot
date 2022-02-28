package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;
import com.graduation.parrot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    @Override
    public Long insert(BoardDto boardDto, User user) {
        return boardRepository.save(new Board(boardDto.getTitle(), boardDto.getContent(), user)).getId();
    }

    @Override
    public Long update(Long board_id, BoardDto boardDto) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("board update - 해당 게시글이 없습니다. id = " + board_id));
        board.update(boardDto.getTitle(), boardDto.getContent());
        boardRepository.save(board);

        return board_id;
    }

    @Override
    public void delete(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("board delete - 해당 게시글이 없습니다. id = " + board_id));
        boardRepository.delete(board);
    }

    @Override
    public BoardResponseDto getBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));

        return new BoardResponseDto(board, board.getUser().getId());
    }

    @Override
    public List<BoardListResponseDto> getBoardList() {
        return boardRepository.findAllByOrderByIdDesc().stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }
}
