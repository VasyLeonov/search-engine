package com.example.web_search_engine.response.impl;

import com.example.web_search_engine.model.dto.Statistics;
import com.example.web_search_engine.response.ResponseService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class StatisticResponse implements ResponseService {

    private boolean result;

    private Statistics statistics;

    @Override
    public boolean result() {
        return false;
    }
}
