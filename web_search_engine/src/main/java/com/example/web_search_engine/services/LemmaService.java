package com.example.web_search_engine.services;

import com.example.web_search_engine.model.Page;

import java.util.List;

public interface LemmaService {
    void createLemmas(List<Page> pages);
}
