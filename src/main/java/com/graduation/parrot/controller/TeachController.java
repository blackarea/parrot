package com.graduation.parrot.controller;

import com.graduation.parrot.webSocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class TeachController {

    @PostMapping("/teach")
    public Map<String, String> teach(@RequestBody Map<String, String> teachParameter) throws InterruptedException {

        teachParameter.put("teach", "a");
        String requestteach = teachParameter.get("teach");
        log.info("requestteach : " + requestteach);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teach"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(requestteach);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseteach = new HashMap<>();
        responseteach.put("teach", pythonMessage);

        return responseteach;
        /* ws://localhost:8888/ws/teach
        ws://localhost:8888/ws/teachPolar
        ws://localhost:8888/ws/teachFree*/
    }

    @GetMapping("/teach/polar")
    public String teachPolar() {

        return "teach/teachPolar";
    }

    @GetMapping("/teach/free")
    public String teachFree() {

        return "teach/teachFree";
    }

    @GetMapping("/mission")
    public String mission(){
        return "teach/mission";
    }
}
