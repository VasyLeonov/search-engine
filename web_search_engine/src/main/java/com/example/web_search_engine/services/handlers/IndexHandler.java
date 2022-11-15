package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.*;
import com.example.web_search_engine.repositories.IndexRepository;
import com.example.web_search_engine.services.impl.LemmaServiceImpl;
import com.example.web_search_engine.services.impl.SiteServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        pages.forEach(page -> {
            String contTitle = lemmaService.titleHtml(page.getContent());
            String contBody = lemmaService.bodyHtml(page.getContent());
            indexes.addAll(buildIndexes(fields, page, contTitle, contBody, lemmaList));
            if(indexes.size() > MAX_INDEXES_IN_LIST) {
                indexRepository.saveAll(indexes);
                indexes.clear();
                webSite.setStatusTime(LocalDateTime.now());
                siteService.putSite(webSite);
            }
        });
        indexRepository.saveAll(indexes);
    }

    public List<Index> buildIndexes(List<Field> fields, Page page, String contTitle, String contBody,
                                    List<Lemma> lemmaList) {

        List<Index> indexes = new ArrayList<>();
        lemmaFinder.getLemmaSet(contTitle.concat(contBody)).forEach(strLemma -> {
            List<Lemma> lemmas = findLemmaInList(strLemma, lemmaList);
            for (Lemma lemma : lemmas) {
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

    public List<Lemma> findLemmaInList(String search, List<Lemma> list) {
        Iterable<Lemma> result = list.stream().filter(s -> Objects.equals(s.getLemma(), search))
                .collect(Collectors.toList());
        return Lists.newArrayList(result.iterator());
    }

    public float calculateRank(List<Field> fields, int titleLemmaCount, int bodyLemmaCount) {
        if (titleLemmaCount == 0) {
            return bodyLemmaCount * fields.get(1).getWeight();
        } else if (bodyLemmaCount == 0) {
            return titleLemmaCount;
        } else {
            return titleLemmaCount + (bodyLemmaCount * fields.get(1).getWeight());
        }
    }
}
