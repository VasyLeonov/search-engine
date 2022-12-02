package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.Index;
import com.example.web_search_engine.model.Lemma;
import com.example.web_search_engine.model.Page;
import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.model.dto.SearchData;
import com.example.web_search_engine.services.impl.IndexServiceImpl;
import com.example.web_search_engine.services.impl.LemmaServiceImpl;
import com.example.web_search_engine.services.impl.PageServiceImpl;
import com.example.web_search_engine.services.impl.SiteServiceImpl;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchHandler {

    private final LemmaFinder lemmaFinder;
    private final LemmaServiceImpl lemmaService;
    private final IndexServiceImpl indexService;
    private final PageServiceImpl pageService;
    private final SiteServiceImpl siteService;
    private int searchCount;

    @Autowired
    public SearchHandler(LemmaServiceImpl lemmaService,
                         IndexServiceImpl indexService,
                         PageServiceImpl pageService,
                         SiteServiceImpl siteService) throws IOException {
        this.lemmaFinder = LemmaFinder.getInstance();
        this.lemmaService = lemmaService;
        this.indexService = indexService;
        this.pageService = pageService;
        this.siteService = siteService;
    }

    public List<Lemma> findLemmasFromRequest(String text, WebSite webSite) {

        List<Lemma> lemmaList = new ArrayList<>();
        Set<String> strings = lemmaFinder.getLemmaSet(text);

        strings.forEach(lem -> {
            List<Lemma> lemmas = lemmaService.getLemmasByLemma(lem);
            if (webSite != null) {
                lemmaList.addAll(lemmas.stream().filter(l ->
                        Objects.equals(l.getSiteId(),
                        webSite.getId())).collect(Collectors.toList()));
                return;
            }
            lemmaList.addAll(lemmas);
        });
        lemmaList.sort(Comparator.comparing(Lemma::getFrequency));
        return lemmaList;
    }

    public List<SearchData> searchData(WebSite webSite, String text, int offset, int limit) {

        List<Lemma> lemmas = findLemmasFromRequest(text, webSite);
        Map<Page, Float> resultPages = new HashMap<>();
        Map<Long, Float> resultPagesId = new HashMap<>();
        Map<Long, Float> pagesIdMap = new HashMap<>();

        List <Index> indexesOneLemma = indexService.getIndexesByLemmaId(lemmas.get(0).getId());
        indexesOneLemma.forEach(index -> pagesIdMap.put(index.getPageId(), index.getRank()));

        for (Lemma lemma : lemmas) {
            List <Index> indexes = indexService.getIndexesByLemmaId(lemma.getId());
            indexes.forEach(index -> {
                Long pageId = index.getPageId();
                resultPagesId.put(pageId,  pagesIdMap.containsKey(pageId) ?
                        pagesIdMap.get(pageId) + index.getRank() : index.getRank());
            });
        }
        List<Long> pagesId = new ArrayList<>();
        sortByValue(resultPagesId).forEach((key, value) -> pagesId.add(key));
        setSearchCount(pagesId.size());

        pagesId.subList(offset, Math.min(pagesId.size(), offset + limit))
                .forEach(pageId -> resultPages.put(pageService
                .getPageById(pageId), resultPagesId.get(pageId)));

        return !resultPages.isEmpty() ? createSearchData(resultPages, lemmas) : new ArrayList<>();
    }

    private List<SearchData> createSearchData(Map<Page, Float> pages, List<Lemma> lemmas) {
        List<SearchData> result = new ArrayList<>();
        calculateRelevance(pages).forEach((key, value) -> {
            String snippet = buildSnippet(key.getContent(), lemmas);
            WebSite site = siteService.getSiteById(key.getSiteId());
            result.add(new SearchData(site.getUrl(), site.getName(), key.getPath(),
                    Jsoup.parse(key.getContent()).title(),
                    snippet, value));
        });
        return result;
    }

    public String buildSnippet(String html, List <Lemma> lemmas) {

        StringBuilder builder = new StringBuilder();
        StringBuilder buildSnippet = new StringBuilder();
        builder.append(Jsoup.parse(html).text());

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ROOT);
        iterator.setText(builder.toString());
        int start = iterator.first();

        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
             String str = builder.substring(start, end);
             buildSnippet.append(substringSearch(str, lemmas));
        }
        return buildSnippet.length() > 550 ? buildSnippet.substring(0, 550).concat("...")
                : buildSnippet.toString();
    }

    public String substringSearch(String str, List <Lemma> lemmas) {

        StringBuilder result = new StringBuilder();

        for (Lemma lemma : lemmas) {
            StringBuilder text = new StringBuilder();
            String strLemma = lemma.getLemma();
            if (str.toLowerCase(Locale.ROOT).contains(strLemma)) {
                String replacement = "<b>".concat(strLemma).concat("</b>");
                text.append(Pattern.compile(strLemma, Pattern.LITERAL |
                         Pattern.CASE_INSENSITIVE |
                         Pattern.UNICODE_CASE).matcher(str)
                         .replaceAll(Matcher.quoteReplacement(replacement)));
            }
            int lastIndex = text.indexOf("<b>");
            int nextIndex = text.indexOf("</b>");
            if ((text.length() - nextIndex) > 50) {
                text.delete(nextIndex + 50, text.length() - 1);
                text.append("...");
            }
            if (lastIndex > 60) {
                text.delete(0, lastIndex);
            }
            result.append(text.append(" "));
        }
        return result.toString();
    }

    public Map<Page, Float> calculateRelevance(Map<Page, Float> pages) {
        float maxValue = Collections.max(pages.entrySet(),
                Comparator.comparingDouble(Map.Entry::getValue)).getValue();
        pages.forEach((key, value) -> pages.put(key, value / maxValue));
        return pages;
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        Map<K,V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K,V>> stream = map.entrySet().stream();
        stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> result.put(e.getKey(),e.getValue()));
        return result;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }
}
