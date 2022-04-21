package com.graduation.parrot.controller;

import com.graduation.parrot.webSocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController {

    @GetMapping()
    public String chat() {
        return "chat/chat";
    }

    @ResponseBody
    @GetMapping("/parrottalk")
    public Map<String, String> parrotFirst() throws InterruptedException {
        String pythonMessage = webSocket("ws://localhost:8888/ws/parrottalk", "parrotTalk");
        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

    @ResponseBody
    @PostMapping("/parrotanswer")
    public Map<String, String> parrotAnswer(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String requestChat = chatParameter.get("question").concat(",").concat(chatParameter.get("answer"));

        String pythonMessage = webSocket("ws://localhost:8888/ws/parrotanswer", requestChat);
        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

    public String webSocket(String url, String sendMessage) throws InterruptedException {
        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create(url), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(sendMessage);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        return pythonMessage;
    }
}
