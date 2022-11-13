package com.example.web_search_engine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController {

    @RequestMapping("/admin")
    public String admin() {
        return "index";
    }

}
