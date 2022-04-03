package com.graduation.parrot.controller;

import com.graduation.parrot.webSocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/* ws://localhost:8888/ws/chat
ws://localhost:8888/ws/teach
ws://localhost:8888/ws/teachPolar
ws://localhost:8888/ws/teachFree*/

@Controller
@Slf4j
public class TeachController {

    @GetMapping("/teach")
    public String teach()  {

        return "teach/teach";

    }

    @PostMapping("/teach")
    @ResponseBody
    public Map<String, String> teach(@RequestBody Map<String, String> teachParameter) throws InterruptedException {
        String questionTeach = teachParameter.get("question");
        String answerTeach = teachParameter.get("answer");

        log.info(questionTeach);
        log.info(answerTeach);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teach"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(questionTeach + "," + answerTeach);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseTeach = new HashMap<>();
        responseTeach.put("teach", pythonMessage);

        return responseTeach;
    }

    @GetMapping("/teach/polar")
    public String teachPolar() {

        return "teach/teachPolar";
    }

    @PostMapping("/teach/polar")
    @ResponseBody
    public Map<String, String> teachPolar(@RequestBody Map<String, String> teachPolarParameter) throws InterruptedException {

        String questionTeachPolar = teachPolarParameter.get("question");
        String postiveTeachPolar = teachPolarParameter.get("postive");
        String negativeTeachPolar = teachPolarParameter.get("negative");

        log.info(questionTeachPolar);
        log.info(postiveTeachPolar);
        log.info(negativeTeachPolar);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teachPolar"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(questionTeachPolar + "/" + postiveTeachPolar + "/" + negativeTeachPolar);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseTeachPolar = new HashMap<>();
        responseTeachPolar.put("teach", pythonMessage);

        return responseTeachPolar;
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
