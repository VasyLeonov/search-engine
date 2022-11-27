package com.example.web_search_engine.controllers;

import com.example.web_search_engine.response.ResponseService;
import com.example.web_search_engine.services.impl.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final SearchServiceImpl searchService;

    @Autowired
    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam(name="query", required=false, defaultValue="") String query,
            @RequestParam(name="site", required=false, defaultValue="") String site,
            @RequestParam(name="offset", required=false, defaultValue="") int offset,
            @RequestParam(name="limit", required=false, defaultValue="") int limit
    ) {
        ResponseService response = searchService.searchContent(query, site, offset, limit);
        return ResponseEntity.ok(response);
    }
}
