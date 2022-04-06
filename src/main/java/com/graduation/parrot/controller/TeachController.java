package com.graduation.parrot.controller;

import com.graduation.parrot.webSocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/* ws://localhost:8888/ws/teachFree*/

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
    public Map<String, String> teachPolar(@RequestBody Map<String, String> teachPolarParameter) throws InterruptedException {

        String parrotQuestionTeach = teachPolarParameter.get("question");
        String positiveAnswerTeach = teachPolarParameter.get("positive");
        String negativeAnswerTeach = teachPolarParameter.get("negative");

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teachpolar"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(parrotQuestionTeach + "," + positiveAnswerTeach + "," + negativeAnswerTeach);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseTeach = new HashMap<>();
        responseTeach.put("teachPolar", pythonMessage);

        return responseTeach;
    }

    @GetMapping("/teach/free")
    public String teachFree() {

        return "teach/teachFree";
    }

    @PostMapping("/teach/free")
    @ResponseBody
    public Map<String, String> teachFree(@RequestBody Map<String, String> teachFreeParameter) throws InterruptedException {

        String parrotQuestionTeach = teachFreeParameter.get("question");
        String answerTeach1 = teachFreeParameter.get("condition1");
        String parrotAnswerTeach1 = teachFreeParameter.get("answer1");
        String answerTeach2 = teachFreeParameter.get("condition2");
        String parrotAnswerTeach2 = teachFreeParameter.get("answer2");

        log.info(parrotQuestionTeach);
        log.info(answerTeach1);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teachfree"), new Draft_6455());
        webSocketUtil.connectBlocking();
        webSocketUtil.send(parrotQuestionTeach + "," + answerTeach1 + "," + parrotAnswerTeach1 + "," + answerTeach2 + "," + parrotAnswerTeach2);
        webSocketUtil.run();
        String pythonMessage = webSocketUtil.getPythonMessage();
        webSocketUtil.close();

        Map<String, String> responseTeach = new HashMap<>();
        responseTeach.put("teach", pythonMessage);

        return responseTeach;
    }

    @GetMapping("/mission")
    public String mission(){
        return "teach/mission";
    }
}
