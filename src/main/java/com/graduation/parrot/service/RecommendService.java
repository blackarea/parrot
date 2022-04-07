package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;
import com.graduation.parrot.domain.dto.RecommendDto;

public interface RecommendService {
    RecommendDto recommend(User user, Long board_id, int point);
}
