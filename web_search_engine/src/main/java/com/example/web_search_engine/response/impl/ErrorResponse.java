package com.example.web_search_engine.response.impl;

import com.example.web_search_engine.response.ResponseService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorResponse implements ResponseService {

    private boolean result;

    private String error;

    @Override
    public boolean result() {
        return false;
    }
}
