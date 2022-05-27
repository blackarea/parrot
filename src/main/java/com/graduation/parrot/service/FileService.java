package com.graduation.parrot.service;

import com.graduation.parrot.domain.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileDto saveFile(MultipartFile multipartFile) throws IOException;
}
