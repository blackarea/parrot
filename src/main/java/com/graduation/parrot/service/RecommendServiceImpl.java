package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Recommend;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final BoardRepository boardRepository;
    public static final int LIKE = 1;
    public static final int HATE = -1;

    @Transactional
    @Override
    public boolean like(User user, Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));

        int point;
        if (isNewRecommend(user, board)) {
            recommendRepository.save(new Recommend(user, board, LIKE));
            point = 1;
        } else {
            Recommend recommend = recommendRepository.findByUserAndBoard(user, board).get();
            point = oldRecommend(recommend, LIKE);
        }
        board.updateRecommendCount(point);

        return point > 0;
    }

    @Transactional
    @Override
    public boolean hate(User user, Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));

        int point;
        if (isNewRecommend(user, board)) {
            recommendRepository.save(new Recommend(user, board, HATE));
            point = -1;
        } else {
            Recommend recommend = recommendRepository.findByUserAndBoard(user, board).get();
            point = oldRecommend(recommend, HATE);
        }
        board.updateRecommendCount(point);

        return point < 0;
    }

    private boolean isNewRecommend(User user, Board board) {
        return recommendRepository.findByUserAndBoard(user, board).isEmpty();
    }

    private int oldRecommend(Recommend recommend, int recommendNumber){
        if (recommend.getPoint() == recommendNumber) {
            recommendRepository.delete(recommend);
            return recommendNumber == LIKE ? -1 : 1;
        } else {
            recommend.updatePoint(recommendNumber);
            return recommendNumber == LIKE ? 2 : -2;
        }
    }

}
