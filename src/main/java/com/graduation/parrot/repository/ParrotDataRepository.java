package com.graduation.parrot.repository;

import com.graduation.parrot.domain.ParrotData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParrotDataRepository extends JpaRepository<ParrotData, Long> {

    @Query("select p from ParrotData p where p.page= :page and p.user.login_id = :login_id")
    Optional<ParrotData> findByPageAndUserLogin_id(@Param("page") int page,@Param("login_id") String login_id);

    @Query("select p from ParrotData p where p.user.login_id = :login_id")
    Optional<List<ParrotData>> findByUserLogin_id(@Param("login_id") String login_id);
}
