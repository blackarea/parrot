package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.QParrotState;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.BoardDto;
import com.graduation.parrot.domain.dto.BoardListResponseDto;
import com.graduation.parrot.domain.dto.BoardResponseDto;
import com.graduation.parrot.repository.BoardRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.graduation.parrot.domain.QBoard.board;
import static com.graduation.parrot.domain.QUser.user;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final JPAQueryFactory queryFactory;

    public BoardServiceImpl(EntityManager em, BoardRepository boardRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.boardRepository = boardRepository;
    }

    @Override
    public Long create(BoardDto boardDto, User user) {
        if(boardDto.getNotice() == null){
            return boardRepository.save(new Board(boardDto.getTitle(), boardDto.getContent(), user, "1")).getId();
        }
        return boardRepository.save(new Board(boardDto.getTitle(), boardDto.getContent(), user, boardDto.getNotice())).getId();
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
    public Page<BoardListResponseDto> getBoardList(Pageable pageable, String array) {
        QueryResults<Board> boardQueryResults = queryFactory
                .selectFrom(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .join(board.user, user).fetchJoin()
                .join(user.parrotState, QParrotState.parrotState).fetchJoin()
                .orderBy(board.notice.desc())
                .orderBy(boardSort(array))
                .orderBy(board.id.desc())
                .fetchResults();

        long total = boardQueryResults.getTotal();
        List<BoardListResponseDto> results = boardQueryResults.getResults().stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, total);
    }

    public Page<BoardListResponseDto> getBoardListPagingSearch(Pageable pageable, String type, String searchKeyword, String array) {

        QueryResults<Board> boardQueryResults = queryFactory
                .selectFrom(board)
                .where(decideWhere(type, searchKeyword))
                .join(board.user, user).fetchJoin()
                .join(user.parrotState, QParrotState.parrotState).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.notice.desc())
                .orderBy(boardSort(array))
                .orderBy(board.id.desc())
                .fetchResults();

        long total = boardQueryResults.getTotal();
        List<BoardListResponseDto> results = boardQueryResults.getResults().stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
        return new PageImpl<>(results, pageable, total);
    }

    @Transactional
    @Override
    public int updateView(Long board_id) {
        return boardRepository.updateView(board_id);
    }

    private BooleanExpression decideWhere(String type, String searchKeyword) {
        if (type.equals("all")) {
            return allEq(searchKeyword);
        } else if (type.equals("title")) {
            return titleEq(searchKeyword);
        } else if (type.equals("content")) {
            return contentEq(searchKeyword);
        } else if (type.equals("user")) {
            return writerEq(searchKeyword);
        } else
            return null;
    }

    private BooleanExpression titleEq(String searchKeyword) {
        return searchKeyword == null ? null : board.title.contains(searchKeyword);
    }

    private BooleanExpression contentEq(String searchKeyword) {
        return searchKeyword == null ? null : board.content.contains(searchKeyword);
    }

    private BooleanExpression writerEq(String searchKeyword) {
        return searchKeyword == null ? null : board.user.name.contains(searchKeyword);
    }

    private BooleanExpression allEq(String searchKeyword) {
        return titleEq(searchKeyword).or(contentEq(searchKeyword).or(writerEq(searchKeyword)));
    }

    private OrderSpecifier<?> boardSort(String array) {
        switch (array) {
            case "all":
                return new OrderSpecifier(Order.DESC, board.id);
            case "recommend":
                return new OrderSpecifier(Order.DESC, board.recommendCount);
            case "view":
                return new OrderSpecifier(Order.DESC, board.view);
        }
        return null;
    }
}
