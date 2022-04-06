package com.graduation.parrot.repository;

import com.graduation.parrot.domain.Board;
import com.graduation.parrot.domain.Recommend;
import com.graduation.parrot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByUserAndBoard(User user, Board board);
}
