package com.graduation.parrot.controller;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.TeachType;
import com.graduation.parrot.domain.dto.TeachFreeDto;
import com.graduation.parrot.service.UserService;
import com.graduation.parrot.webSocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class TeachController {

    private final UserService userService;
    WebSocketService webSocketService = new WebSocketService();

    @GetMapping("/teach")
    public String teach() {
        return "teach/teach";
    }

    @PostMapping("/teach")
    @ResponseBody
    public void teach(@RequestBody Map<String, String> teachParameter,
                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws InterruptedException {
        String teaching = teachParameter.get("question") + "," + teachParameter.get("answer");

        webSocketService.send(teaching, "ws://localhost:8888/ws/teach");
        userService.saveTeach(principalDetails.getUser().getLogin_id(), TeachType.NORMAL, teaching);
    }

    @GetMapping("/teach/polar")
    public String teachPolar() {
        return "teach/teachPolar";
    }

    @PostMapping("/teach/polar")
    @ResponseBody
    public void teachPolar(@RequestBody Map<String, String> teachPolarParameter,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws InterruptedException {
        String teaching = teachPolarParameter.get("question") + "," +
                teachPolarParameter.get("positive") + "," + teachPolarParameter.get("negative");

        webSocketService.send(teaching, "ws://localhost:8888/ws/teachpolar");
        userService.saveTeach(principalDetails.getUser().getLogin_id(), TeachType.POLAR, teaching);
    }

    @GetMapping("/teach/free")
    public String teachFree() {
        return "teach/teachFree";
    }

    @PostMapping("/teach/free")
    @ResponseBody
    public void teachFree(@RequestBody TeachFreeDto teachFreeDto,
                          @AuthenticationPrincipal PrincipalDetails principalDetails) throws InterruptedException {
        String conditionAnswer = "";
        List<TeachFreeDto.TeachFreeList> condition = teachFreeDto.getCondition();
        List<TeachFreeDto.TeachFreeList> answer = teachFreeDto.getAnswer();

        for (int i = 0; i < teachFreeDto.getAnswerCount(); i++) {
            conditionAnswer = conditionAnswer.concat(condition.get(i).getContent())
                    .concat(",").concat(answer.get(i).getContent()).concat(",");
        }

        String teaching = teachFreeDto.getQuestion() + "," + conditionAnswer + teachFreeDto.getNotIncludeAnswer();

        webSocketService.send(teaching, "ws://localhost:8888/ws/teachfree");
        userService.saveTeach(principalDetails.getUser().getLogin_id(), TeachType.FREE, teaching);
    }

    @GetMapping("/mission")
    public String mission() {
        return "teach/mission";
    }
}
