package com.graduation.parrot.controller;

import com.graduation.parrot.webSocket.WebSocketService;
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

    WebSocketService webSocketService = new WebSocketService();

    @GetMapping()
    public String chat() {
        return "chat/chat";
    }

    @ResponseBody
    @PostMapping()
    public Map<String, String> chat(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String requestChat = chatParameter.get("chat");

        String pythonMessage = webSocketService.sendWebSocket(requestChat, "ws://localhost:8888/ws/web/chat");

        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

    @ResponseBody
    @GetMapping("/parrottalk")
    public Map<String, String> parrotFirst() throws InterruptedException {
        String pythonMessage = webSocketService.sendWebSocket("parrotTalk", "ws://localhost:8888/ws/parrottalk");
        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

    @ResponseBody
    @PostMapping("/parrotanswer")
    public Map<String, String> parrotAnswer(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String requestChat = chatParameter.get("question").concat(",").concat(chatParameter.get("answer"));

        String pythonMessage = webSocketService.sendWebSocket(requestChat, "ws://localhost:8888/ws/parrotanswer");
        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

}
