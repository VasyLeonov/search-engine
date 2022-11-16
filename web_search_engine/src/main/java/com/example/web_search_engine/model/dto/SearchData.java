package com.example.web_search_engine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class SearchData {

    private String site;

    private String siteName;

    private String uri;

    private String title;

    private String snippet;

    private Float relevance;
}
