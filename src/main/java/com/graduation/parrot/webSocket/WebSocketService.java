package com.graduation.parrot.webSocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;

@Slf4j
public class WebSocketService {

    public String sendWebSocket(String message, String url) throws InterruptedException {
        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create(url), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(message);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();

        log.info("spring to python : "+ message);
        log.info("python to spring : "+pythonMessage);

        webSocketUtil.close();
        return pythonMessage;
    }
}
