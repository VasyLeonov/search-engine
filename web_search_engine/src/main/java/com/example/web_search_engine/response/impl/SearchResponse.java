package com.example.web_search_engine.response.impl;

import com.example.web_search_engine.model.dto.SearchData;
import com.example.web_search_engine.response.ResponseService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SearchResponse implements ResponseService {

    private boolean result;

    private int count;

    private List<SearchData> data;

    @Override
    public boolean result() {
        return false;
    }
}
