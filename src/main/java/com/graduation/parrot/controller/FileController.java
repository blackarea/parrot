package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.FileDto;
import com.graduation.parrot.service.FileService;
import com.graduation.parrot.webSocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private WebSocketService webSocketService = new WebSocketService();

    @PostMapping("/file")
    public FileDto storeFile(@RequestParam("file")MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        FileDto fileDto = fileService.saveFile(multipartFile);
        log.info("fileDto = {}", fileDto);
        return fileDto;
    }

    @PostMapping("/file/sendimage")
    public void sendImage(@RequestBody Map<String, String> image) throws InterruptedException {
        String encodedImage = image.get("image");
        webSocketService.send(encodedImage, "ws://localhost:8888/ws/image");
    }
}
