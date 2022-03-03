package com.graduation.parrot.controller;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ChatController {

    @PostMapping("/app/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> chatParameter){
        String requestChat = chatParameter.get("chat");
        Map<String, String> responseChat = new HashMap<>();

        WebSocketClient webSocketClient = new WebSocketClient(URI.create("ws://localhost:8888/ws/chat"), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                this.send(requestChat);
            }

            @Override
            public void onMessage(String message) {
                System.out.println("ChatController.onMessage");
                System.out.println("python to springboot message = " + message);
                responseChat.put("chat", message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        };
        webSocketClient.connect();
        webSocketClient.close();

        return responseChat;
    }
}
