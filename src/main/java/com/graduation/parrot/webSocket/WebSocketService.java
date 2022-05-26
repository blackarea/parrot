package com.graduation.parrot.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.util.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

@Slf4j
public class WebSocketService {

    public void send(String message, String url) throws InterruptedException {
        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create(url), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(message);
        webSocketUtil.run();

        log.info("spring to python : " + message);

        webSocketUtil.close();
    }

    public String sendAndReceive(String message, String url) throws InterruptedException {
        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create(url), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(message);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();

        log.info("spring to python : " + message);
        log.info("python to spring : " + pythonMessage);

        webSocketUtil.close();
        return pythonMessage;
    }

    public void sendImage(MultipartFile multipartFile, String url) throws InterruptedException, IOException {
        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create(url), new Draft_6455());
        webSocketUtil.connectBlocking();

        File file = convertToFile(multipartFile);
        byte[] bytes = Files.readAllBytes(file.toPath());

        String s = Base64.encodeBytes(bytes);
        // ByteBuffer 를 클라이언트로 보낸다.
        webSocketUtil.send(s);
        webSocketUtil.run();
        webSocketUtil.close();
    }

    public File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

}
