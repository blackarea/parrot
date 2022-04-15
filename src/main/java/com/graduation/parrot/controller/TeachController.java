package com.graduation.parrot.controller;

import com.graduation.parrot.domain.dto.TeachFreeDto;
import com.graduation.parrot.webSocket.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.drafts.Draft_6455;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
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
    @ResponseBody
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
    public Map<String, String> teachFree(@RequestBody TeachFreeDto teachFreeDto) throws InterruptedException {

        log.info(parrotQuestionTeach);
        log.info(answerTeach1);

        WebSocketUtil webSocketUtil = new WebSocketUtil(URI.create("ws://localhost:8888/ws/teachfree"), new Draft_6455());
        webSocketUtil.connectBlocking();
        String conditionAnswer = "";

        List<TeachFreeDto.TeachFreeList> condition = teachFreeDto.getCondition();
        List<TeachFreeDto.TeachFreeList> answer = teachFreeDto.getAnswer();

        for (int i = 0; i < teachFreeDto.getAnswerCount(); i++) {
            conditionAnswer = conditionAnswer.concat(condition.get(i).getContent())
                    .concat(",").concat(answer.get(i).getContent()).concat(",");
        }

        webSocketUtil.send(teachFreeDto.getQuestion() + "," + conditionAnswer + teachFreeDto.getNotIncludeAnswer());
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
