package com.example.web_search_engine.response.impl;

import com.example.web_search_engine.response.ResponseService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Response implements ResponseService {

    private boolean result;

    @Override
    public boolean result() {
        return true;
    }
}
