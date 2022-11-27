package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.model.dto.Detailed;
import com.example.web_search_engine.model.dto.Statistics;
import com.example.web_search_engine.model.dto.Total;
import com.example.web_search_engine.services.StatisticService;
import com.example.web_search_engine.response.impl.StatisticResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final PageServiceImpl pageService;
    private final SiteServiceImpl siteService;
    private final LemmaServiceImpl lemmaService;
    private final IndexServiceImpl indexService;

    @Autowired
    public StatisticServiceImpl(PageServiceImpl pageService,
                                SiteServiceImpl siteService,
                                LemmaServiceImpl lemmaService,
                                IndexServiceImpl indexService) {
        this.pageService = pageService;
        this.siteService = siteService;
        this.lemmaService = lemmaService;
        this.indexService = indexService;
    }

    public Statistics buildStatistics() {
        Statistics statistics = new Statistics();
        Total total = new Total();
        total.setSites(siteService.getCountSites());
        total.setPages(pageService.getPagesCount());
        total.setLemmas(lemmaService.getLemmaCount());
        total.setIndexing(indexService.isIndexing());
        statistics.setTotal(total);
        statistics.setDetailed(detailedSites());
        return statistics;
    }

    public List<Detailed> detailedSites() {
        List<Detailed> detailedList = new ArrayList<>();
        List<WebSite> sites = siteService.getAllSites();
        sites.forEach(site -> {
            Detailed detailed = new Detailed();
            detailed.setUrl(site.getUrl());
            detailed.setName(site.getName());
            detailed.setStatus(site.getStatus());
            detailed.setStatusTime(site.getStatusTime());
            detailed.setError(site.getLastError());
            detailed.setLemmas(lemmaService.getCountBySiteId(site.getId()));
            detailed.setPages(pageService.getCountBySiteId(site.getId()));
            detailedList.add(detailed);
        });
        return detailedList;
    }

    @Override
    public StatisticResponse getStatistics() {
        Statistics statistic = buildStatistics();
        StatisticResponse response = new StatisticResponse();
        response.setStatistics(statistic);
        response.setResult(true);
        return response;
    }
}
