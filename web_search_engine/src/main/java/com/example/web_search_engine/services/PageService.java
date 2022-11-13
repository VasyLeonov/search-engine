package com.example.web_search_engine.services;

import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.services.impl.SiteServiceImpl;

public interface PageService {
    void startCrawlSites(WebSite site, String userAgent, SiteServiceImpl siteService);
}
