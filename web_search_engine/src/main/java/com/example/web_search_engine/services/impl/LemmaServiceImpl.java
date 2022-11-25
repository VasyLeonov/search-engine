package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.Lemma;
import com.example.web_search_engine.model.Page;
import com.example.web_search_engine.repositories.LemmaRepository;
import com.example.web_search_engine.services.handlers.LemmaFinder;
import com.example.web_search_engine.services.LemmaService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;

    private final LemmaFinder lemmaFinder;

    @Autowired
    public LemmaServiceImpl(LemmaRepository lemmaRepository) throws IOException {
        this.lemmaRepository = lemmaRepository;
        this.lemmaFinder = LemmaFinder.getInstance();
    }

    @Override
    public void createLemmas(List<Page> pages) {

        Set<Lemma> lemmas = new HashSet<>();
        pages.forEach(page -> {
            String textTitle = titleHtml(page.getContent());
            String textBody = bodyHtml(page.getContent());
            Set<String> lemmasSet = lemmaFinder.getLemmaSet(textTitle.concat(" ")
                    .concat(textBody));
            lemmasSet.forEach(lem -> {
                Lemma newLemma = new Lemma();
                newLemma.setLemma(lem);
                newLemma.setSiteId(page.getSiteId());
                newLemma.setFrequency(1);
                Lemma lemma = lemmas.stream().filter(l -> l.getLemma().equals(newLemma.getLemma()))
                        .findFirst().orElse(null);
                if (lemma == null) {
                    lemmas.add(newLemma);
                } else {
                    lemma.setFrequency(lemma.getFrequency() + 1);
                    lemmas.add(lemma);
                }
            });
        });
        lemmaRepository.saveAll(lemmas);
    }

    public Lemma putLemma(Lemma lemma) {
        return lemmaRepository.save(lemma);
    }

    public Long getCountBySiteId(Long siteId) {
        return lemmaRepository.findLemmaCountBySiteId(siteId);
    }

    public Long getLemmaCount() {
        return lemmaRepository.count();
    }

    public String bodyHtml(String html) {
        return Jsoup.parseBodyFragment(html).text();
    }

    public String titleHtml(String html) {
        return Jsoup.parse(html).title();
    }

    public List<Lemma> getLemmasByLemma(String lemma) {
        return lemmaRepository.findByLemma(lemma);
    }

    public List<Lemma> getAllLemmasBySiteId(Long siteId) {
        return lemmaRepository.findLemmaBySiteId(siteId);
    }

    public void deleteLemmasBySiteId(Long siteId) {
        lemmaRepository.deleteAllBySiteId(siteId);
    }
}
