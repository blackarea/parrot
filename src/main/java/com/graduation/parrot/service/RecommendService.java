package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.RecommendDto;

import java.util.Map;

public interface RecommendService {
    RecommendDto recommend(User user, Long board_id, int point);
    Map<String, String> alreadyRecommendCheck(User user, Long board_id);
}
