package com.graduation.parrot.repository;

import com.graduation.parrot.domain.ChatData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatDataRepository extends JpaRepository<ChatData, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("select cd from ChatData cd where cd.user.login_id = :login_id")
    List<ChatData> findAllByUserId(@Param("login_id") String login_id);

    @EntityGraph(attributePaths = {"user"})
    @Query("select cd from ChatData cd where cd.user.login_id = :login_id and cd.id = :chatData_id")
    ChatData findByUserIdAndId(@Param("login_id") String login_id, @Param("chatData_id") Long chatData_id);
}
