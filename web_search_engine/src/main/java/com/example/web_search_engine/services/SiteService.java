package com.example.web_search_engine.services;

import com.example.web_search_engine.model.WebSite;

import java.util.List;

public interface SiteService {

    List<WebSite> getAllSites();
    WebSite getSiteById(long id);
    void putSite(WebSite site);
    Long getCountSites();
}
