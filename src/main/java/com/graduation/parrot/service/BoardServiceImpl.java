package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.form.BoardForm;
import com.graduation.parrot.domain.form.BoardListResponseForm;
import com.graduation.parrot.domain.form.BoardResponseForm;
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
    public Long insert(BoardForm boardForm, User user) {
        return boardRepository.save(new Board(boardForm.getTitle(), boardForm.getContent(), user)).getId();
    }

    @Override
    public Long update(Long board_id, BoardForm boardForm) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("board update - 해당 게시글이 없습니다. id = " + board_id));
        board.update(boardForm.getTitle(), boardForm.getContent());
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
    public BoardResponseForm getBoard(Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));
        return new BoardResponseForm(board);
    }

    @Override
    public List<BoardListResponseForm> getBoardList() {
        return boardRepository.findAllByOrderByIdDesc().stream()
                .map(BoardListResponseForm::new)
                .collect(Collectors.toList());
    }
}
