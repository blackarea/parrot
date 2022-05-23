package com.graduation.parrot.repository;

import com.graduation.parrot.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
