package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.FileDto;
import com.graduation.parrot.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping("/file")
    public String storeFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {

        FileDto fileDto = fileService.storeFile(multipartFile);
        System.out.println("multipartFile = " + multipartFile);
        System.out.println("multipartFile = " + multipartFile.getName());
        System.out.println("multipartFile = " + multipartFile.getContentType());

        return null;
    }
}
