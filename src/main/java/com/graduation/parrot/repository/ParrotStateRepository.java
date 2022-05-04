package com.graduation.parrot.repository;

import com.graduation.parrot.domain.ParrotState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParrotStateRepository extends JpaRepository<ParrotState, Long> {
}
