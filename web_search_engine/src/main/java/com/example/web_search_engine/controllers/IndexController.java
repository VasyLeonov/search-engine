package com.example.web_search_engine.controllers;

import com.example.web_search_engine.services.impl.IndexServiceImpl;
import com.example.web_search_engine.response.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    private final IndexServiceImpl indexService;

    @Autowired
    public IndexController(IndexServiceImpl indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Object> startIndexing() {
        ResponseService response = indexService.indexingAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Object> stopIndexing() {
        ResponseService response = indexService.stopIndexing();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Object> indexPage(@RequestParam(name="url", required=false, defaultValue="") String url) {
        ResponseService response = indexService.indexingOne(url);
        return ResponseEntity.ok(response);
    }
}
