package com.graduation.parrot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TeachController {

    @GetMapping("/teach")
    public String teach(){
        return "teach/teach";
    }

    @GetMapping("/teach/polar")
    public String teachPolar(){
        return "teach/teachPolar";
    }

    @GetMapping("/teach/free")
    public String teachFree(){
        return "teach/teachFree";
    }

    @GetMapping("/mission")
    public String mission(){
        return "teach/mission";
    }
}
