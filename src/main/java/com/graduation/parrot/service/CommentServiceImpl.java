package com.graduation.parrot.service;

import com.graduation.parrot.domain.*;
import com.graduation.parrot.domain.dto.CommentResponseDto;
import com.graduation.parrot.domain.dto.RecommendDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.CommentRecommendRepository;
import com.graduation.parrot.repository.CommentRepository;
import com.graduation.parrot.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.graduation.parrot.domain.QComment.comment;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRecommendRepository commentRecommendRepository;
    private final JPAQueryFactory queryFactory;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                              BoardRepository boardRepository, CommentRecommendRepository commentRecommendRepository, EntityManager entityManager) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.commentRecommendRepository = commentRecommendRepository;
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Transactional
    @Override
    public Long create(User user, Long board_id, String content) {
        User foundUser = userRepository.findById(user.getId()).get();
        Board board = boardRepository.findById(board_id).get();
        board.updateCommentCount(1);

        return commentRepository.save(new Comment(content, foundUser, board)).getId();
    }

    @Override
    public Comment getComment(Long comment_id) {
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));
        return comment;
    }

    @Transactional
    @Override
    public void update(Long comment_id, String content) {
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));
        comment.update(content);
    }

    @Override
    public void delete(Long board_id, Long comment_id) {
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new NoSuchElementException("해당 댓글이 없습니다."));

        Board board = boardRepository.findById(board_id).get();
        board.updateCommentCount(-1);
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponseDto> getCommentList(Long board_id) {
        List<Comment> commentList = queryFactory
                .selectFrom(comment)
                .where(comment.board.id.eq(board_id))
                .fetch();

        List<CommentResponseDto> commentDtoList =
                commentList.stream().map(CommentResponseDto::new).collect(Collectors.toList());

        return commentDtoList;
    }

    @Transactional
    @Override
    public RecommendDto recommend(User user, Long comment_id, int point) {
        Comment comment = commentRepository.findById(comment_id).get();

        if (alreadyExistRecommend(user, comment)) {
            return new RecommendDto(false);
        }
        commentRecommendRepository.save(new CommentRecommend(user, comment, point));
        comment.updateRecommend(point);
        return new RecommendDto(true, comment.getRecommendCount());
    }

    @Override
    public Map<String, String> recommendCheck(User user, Long comment_id) {
        Comment comment = commentRepository.findById(comment_id).get();
        Map<String, String> map = new HashMap<>();

        Optional<CommentRecommend> presentRecommend = commentRecommendRepository.findByUserAndComment(user, comment);
        if(presentRecommend.isPresent()){
            CommentRecommend commentRecommend = presentRecommend.get();
            map.put("recommend", "yes");
            map.put("point", String.valueOf(commentRecommend.getPoint()));
            return map;
        }

        map.put("recommend", "no");
        return map;
    }

    private boolean alreadyExistRecommend(User user, Comment comment) {
        return commentRecommendRepository.findByUserAndComment(user, comment).isPresent();
    }


}
