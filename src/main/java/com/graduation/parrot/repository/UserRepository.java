package com.graduation.parrot.repository;


import com.graduation.parrot.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"boards"})
    @Query("select u from User u where u.login_id = :login_id")
    Optional<User> findByLogin_id(@Param("login_id") String login_id);
}
