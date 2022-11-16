package com.example.web_search_engine.services;

import com.example.web_search_engine.response.ResponseService;

public interface SearchService {
    ResponseService searchContent(String query, String site, int offset, int limit);
}
