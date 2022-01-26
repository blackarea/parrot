package com.graduation.parrot.repository;


import com.graduation.parrot.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
