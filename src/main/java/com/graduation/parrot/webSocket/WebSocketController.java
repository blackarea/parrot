package com.graduation.parrot.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class WebSocketController {

    WebSocketService webSocketService = new WebSocketService();

    @PostMapping("/app/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String login_id = chatParameter.get("login_id");
        String requestChat = chatParameter.get("chat");
;
        String pythonMessage = webSocketService.sendWebSocket(login_id.concat(",").concat(requestChat),
                "ws://localhost:8888/ws/app/chat");

        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
    }

    @DeleteMapping("/app/chat")
    public void deleteChat(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String login_id = chatParameter.get("login_id");
        String requestChat = chatParameter.get("chat");

        webSocketService.sendWebSocket(login_id.concat(",").concat(requestChat), "ws://localhost:8888/ws/delete");
    }

    @PostMapping("/app/parrottalk")
    public Map<String, String> parrotTalk(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        String state = chatParameter.get("state");

        String pythonMessage = webSocketService.sendWebSocket(state, "ws://localhost:8888/ws/app/parrottalk");

        String[] splitPythonMessage = pythonMessage.split(",");

        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("question", splitPythonMessage[0]);
        responseChat.put("answer", splitPythonMessage[1]);

        return responseChat;
    }
}
