package com.graduation.parrot.service;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Recommend;
import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.RecommendDto;
import com.graduation.parrot.repository.BoardRepository;
import com.graduation.parrot.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
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
    public RecommendDto recommend(User user, Long board_id, int point) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));

        if (alreadyExistRecommend(user, board)) {
            return new RecommendDto(false);
        }
        recommendRepository.save(new Recommend(user, board, point));
        board.updateRecommendCount(point);
        return new RecommendDto(true, board.getRecommendCount());
    }

    @Override
    public Map<String, String> alreadyRecommendCheck(User user, Long board_id) {
        Board board = boardRepository.findById(board_id)
                .orElseThrow(() -> new NoSuchElementException("getBoard - 해당 게시글이 없습니다. id = " + board_id));
        Map<String, String> map = new HashMap<>();

        if (alreadyExistRecommend(user, board)){
            Recommend recommend = recommendRepository.findByUserAndBoard(user, board).get();
            map.put("recommend", "yes");
            map.put("point", String.valueOf(recommend.getPoint()));
            return map;
        }
        map.put("recommend", "no");
        return map;
    }


    private boolean alreadyExistRecommend(User user, Board board) {
        return recommendRepository.findByUserAndBoard(user, board).isPresent();
    }

}
