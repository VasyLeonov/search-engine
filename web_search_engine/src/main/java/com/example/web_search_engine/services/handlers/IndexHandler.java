package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.*;
import com.example.web_search_engine.repositories.IndexRepository;
import com.example.web_search_engine.services.impl.LemmaServiceImpl;
import com.example.web_search_engine.services.impl.SiteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class IndexHandler {

    private static final int MAX_INDEXES_IN_LIST = 1000;
    private final IndexRepository indexRepository;
    private final LemmaServiceImpl lemmaService;
    private final LemmaFinder lemmaFinder;
    private final SiteServiceImpl siteService;

    @Autowired
    public IndexHandler(IndexRepository indexRepository,
                        LemmaServiceImpl lemmaService,
                        SiteServiceImpl siteService) throws IOException {
        this.indexRepository = indexRepository;
        this.lemmaService = lemmaService;
        this.siteService = siteService;
        this.lemmaFinder = LemmaFinder.getInstance();
    }

    public void createIndexes(List<Field> fields, List<Page> pages, WebSite webSite) {
        List<Index> indexes = new ArrayList<>();
        List<Lemma> lemmaList = lemmaService.getAllLemmasBySiteId(webSite.getId());
        HashMap<String, Lemma> lemmasMap = lemmasToMap(lemmaList);
        pages.forEach(page -> {
            String contTitle = lemmaService.titleHtml(page.getContent());
            String contBody = lemmaService.bodyHtml(page.getContent());
            indexes.addAll(buildIndexes(fields, page, contTitle, contBody, lemmasMap));
            if(indexes.size() > MAX_INDEXES_IN_LIST) {
                webSite.setStatusTime(LocalDateTime.now());
                siteService.putSite(webSite);
                indexRepository.saveAll(indexes);
                indexes.clear();
            }
        });
        indexRepository.saveAll(indexes);
    }

    public HashMap<String, Lemma> lemmasToMap(List<Lemma> lemmaList) {
        HashMap<String, Lemma> lemmasMap = new HashMap<>();
        lemmaList.forEach(lemma -> lemmasMap.put(lemma.getLemma(), lemma));
        return lemmasMap;
    }

    public List<Index> buildIndexes(List<Field> fields, Page page, String contTitle, String contBody,
                                    HashMap<String, Lemma> lemmasMap) {
        List<Index> indexes = new ArrayList<>();
        lemmaFinder.getLemmaSet(contTitle.concat(contBody)).forEach(strLemma -> {
            Lemma lemma = lemmasMap.get(strLemma);
            if (lemma != null) {
                int countLemmaTitle = countLemmaTitle(contTitle, strLemma);
                int countLemmaBody = countLemmaBody(contBody, strLemma);
                Index index = new Index();
                index.setPageId(page.getId());
                index.setLemmaId(lemma.getId());
                index.setRank(calculateRank(fields, countLemmaTitle, countLemmaBody));
                indexes.add(index);
            }
        });
        return indexes;
    }

    public int countLemmaTitle(String contentTitle, String strLemma) {
        return lemmaFinder.collectLemmas(contentTitle).get(strLemma) == null ? 0
                : lemmaFinder.collectLemmas(contentTitle).get(strLemma);
    }

    public int countLemmaBody(String contentBody, String strLemma) {
        return lemmaFinder.collectLemmas(contentBody).get(strLemma) == null ? 0
                : lemmaFinder.collectLemmas(contentBody).get(strLemma);
    }

    public float calculateRank(List<Field> fields, int titleLemmaCount, int bodyLemmaCount) {
        if (titleLemmaCount == 0) {
            return bodyLemmaCount * fields.get(1).getWeight();
        } else if (bodyLemmaCount == 0) {
            return titleLemmaCount;
        }
        return titleLemmaCount + (bodyLemmaCount * fields.get(1).getWeight());
    }
}
