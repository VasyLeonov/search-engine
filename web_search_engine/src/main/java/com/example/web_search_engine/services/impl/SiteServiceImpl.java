package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.Status;
import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.repositories.SiteRepository;
import com.example.web_search_engine.services.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;


    @Autowired
    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public void updateStatusTime(Long id) {
        WebSite site = getSiteById(id);
        site.setStatusTime(LocalDateTime.now());
        siteRepository.save(site);
    }

    @Override
    public void updateStatus(Long id, Status status) {
        WebSite site = getSiteById(id);
        site.setStatus(status);
        siteRepository.save(site);
    }

    @Override
    public List<WebSite> getAllSites() {
        return siteRepository.findAll();
    }

    public WebSite getWebSiteByUrl(String url) {
        return siteRepository.findByUrl(url);
    }

    @Override
    public void putToDbSites(List<WebSite> sites) {
         siteRepository.saveAll(sites);

    }

    @Override
    public WebSite getSiteById(long id) {
        return siteRepository.getById(id);
    }

    @Override
    public Long putSite(WebSite site) {
        return siteRepository.save(site).getId();
    }

    @Override
    public Long getCountSites() {
        return siteRepository.count();
    }

    public void deleteSite(Long siteId) {
        siteRepository.deleteById(siteId);
    }
}
