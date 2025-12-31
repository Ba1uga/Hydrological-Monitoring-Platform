package com.baluga.module.waterproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/waterproject")
    public String index() {
        return "redirect:/waterproject/waterproject.html";
    }
}
