package com.example.web_search_engine.model.dto;

import com.example.web_search_engine.model.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Detailed {
    private String url;
    private String name;
    private Enum<Status> status;
    private LocalDateTime statusTime;
    private String error;
    private Long pages;
    private Long lemmas;
}
