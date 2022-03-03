package com.graduation.parrot.chat;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
@Getter
public class WebSocketUtil extends WebSocketClient {
    private String pythonMessage;

    public WebSocketUtil(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        log.info("python to spring boot message : " + message);
        pythonMessage = message;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {
        log.error(ex.getMessage());
    }
}
