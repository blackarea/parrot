package com.graduation.parrot.repository;

import com.graduation.parrot.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = {"user"})
    Page<Board> findAllByOrderByIdDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Board findBoardById(Long id);

    @Modifying
    @Query("update Board b set b.view = b.view +1 where b.id = :id")
    int updateView(@Param("id") Long id);
}
