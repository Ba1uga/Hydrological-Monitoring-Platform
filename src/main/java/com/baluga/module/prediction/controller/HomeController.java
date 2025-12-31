package com.baluga.module.prediction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prediction")
public class HomeController {

    @GetMapping({"", "/"})
    public String home() {
        return "redirect:/prediction/prediction.html";
    }

    @GetMapping("/wyy")
    public String wyy() {
        return "redirect:/prediction/prediction.html";
    }
}
