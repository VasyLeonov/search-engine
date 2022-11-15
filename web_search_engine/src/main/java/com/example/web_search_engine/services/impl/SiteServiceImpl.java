package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.repositories.SiteRepository;
import com.example.web_search_engine.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;

    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public List<WebSite> getAllSites() {
        return siteRepository.findAll();
    }

    public WebSite getWebSiteByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    @Override
    public WebSite getSiteById(long id) {
        return siteRepository.getById(id);
    }

    @Override
    public void putSite(WebSite site) {
        siteRepository.save(site);
    }

    @Override
    public Long getCountSites() {
        return siteRepository.count();
    }

    public void deleteSite(Long siteId) {
        siteRepository.deleteById(siteId);
    }
}
