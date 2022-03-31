package com.graduation.parrot.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class WebSocketController {

    @PostMapping("/app/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> chatParameter) throws InterruptedException {
        log.info("chatParameter" + chatParameter.get("chat"));
        String requestChat = chatParameter.get("chat");
        log.info("requestChat : " + requestChat);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/chat"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(requestChat);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseChat = new HashMap<>();
        responseChat.put("chat", pythonMessage);

        return responseChat;
        /* ws://localhost:8888/ws/teach
        ws://localhost:8888/ws/teachPolar
        ws://localhost:8888/ws/teachFree*/
    }
}
