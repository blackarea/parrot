package com.graduation.parrot.service;

import com.graduation.parrot.domain.User;

public interface RecommendService {
    boolean like(User user, Long board_id);
    boolean hate(User user, Long board_id);
}
