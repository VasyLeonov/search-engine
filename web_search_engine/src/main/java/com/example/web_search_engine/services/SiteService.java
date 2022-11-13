package com.example.web_search_engine.services;

import com.example.web_search_engine.model.Status;
import com.example.web_search_engine.model.WebSite;

import java.util.List;

public interface SiteService {
    List<WebSite> getAllSites();
    void putToDbSites(List<WebSite> sites);
    WebSite getSiteById(long id);
    Long putSite(WebSite site);
    Long getCountSites();
    void updateStatus(Long id, Status status);
    void updateStatusTime(Long id);
}
