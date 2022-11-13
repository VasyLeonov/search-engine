package com.example.web_search_engine.services;


import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.response.ResponseService;

public interface IndexService {
    void indexingSite(WebSite site);
    ResponseService indexingAll();
    ResponseService indexingOne(String url);
    ResponseService stopIndexing();
}
