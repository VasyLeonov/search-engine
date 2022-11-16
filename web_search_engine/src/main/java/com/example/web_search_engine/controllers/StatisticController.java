package com.example.web_search_engine.controllers;

import com.example.web_search_engine.services.impl.StatisticServiceImpl;
import com.example.web_search_engine.response.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticController {

    private final StatisticServiceImpl statisticService;

    @Autowired
    public StatisticController(StatisticServiceImpl statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> statistics() {
        ResponseService response = statisticService.getStatistics();
        return ResponseEntity.ok(response);
    }
}
