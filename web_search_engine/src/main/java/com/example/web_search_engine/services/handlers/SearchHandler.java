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
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class SearchHandler {

    private final LemmaFinder lemmaFinder;
    private final LemmaServiceImpl lemmaService;
    private final IndexServiceImpl indexService;
    private final PageServiceImpl pageService;
    private final SiteServiceImpl siteService;

    @Autowired
    public SearchHandler(@Lazy LemmaServiceImpl lemmaService,
                         IndexServiceImpl indexService,
                         PageServiceImpl pageService,
                         SiteServiceImpl siteService) throws IOException {
        this.lemmaFinder = LemmaFinder.getInstance();
        this.lemmaService = lemmaService;
        this.indexService = indexService;
        this.pageService = pageService;
        this.siteService = siteService;
    }

    public List<Lemma> findLemmasFromRequest(String text) {

        List<Lemma> lemmaList = new ArrayList<>();
        Set<String> strings = lemmaFinder.getLemmaSet(text);
        strings.forEach(lem -> {
            List<Lemma> lemmas = lemmaService.findLemmasByLemma(lem);
            lemmaList.addAll(lemmas);
        });
        lemmaList.sort(Comparator.comparing(Lemma::getFrequency));
        return lemmaList;
    }

    public List<SearchData> searchData(String text) {

        List<Lemma> lemmas = findLemmasFromRequest(text);
        Map<Page, Float> pages = new HashMap<>();
        for (Lemma lemma : lemmas) {
            Map<Page, Float> nextPages = new HashMap<>();
            List <Index> indexes = indexService.getIndexesByLemmaId(lemma.getId());
            indexes.forEach(index -> {
                Optional<Page> page = pageService.getPageById(index.getPageId());
                page.ifPresent(value -> nextPages.put(value, index.getRank()));
            });
            if (pages.isEmpty()) {
                pages.putAll(nextPages);
            }
            Map<Page, Float> interPages = new HashMap<>();
            nextPages.forEach((key, value) -> {
                if (pages.containsKey(key)) {
                    interPages.put(key, pages.get(key) + value);
                }
            });
            pages.clear();
            pages.putAll(interPages);
        }
        if (!pages.isEmpty()) {
           return createSearchData(pages, lemmas);
        } else {
           return new ArrayList<>();
        }
    }

    public List<SearchData> createSearchData(Map<Page, Float> pages, List<Lemma> lemmas) {

        List<SearchData> result = new ArrayList<>();
        calculateRelevance(pages).forEach((key, value) -> {
            String snippet = buildSnippet(key.getContent(), lemmas);
            if (!snippet.isEmpty()) {
                WebSite site = siteService.getSiteById(key.getSiteId());
                result.add(new SearchData(site.getUrl(), site.getName(), key.getPath(),
                        Jsoup.parse(key.getContent()).title(),
                        snippet, value));
            }
        });
        return result;
    }

    public String buildSnippet(String html, List <Lemma> lemmas) {

        StringBuilder builder = new StringBuilder();
        StringBuilder buildSnippet = new StringBuilder();
        Document document = Jsoup.parse(html);
        builder.append(document.text());

        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ROOT);
        iterator.setText(builder.toString());
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
             String str = builder.substring(start, end);
             buildSnippet.append(substringSearch(str, lemmas));
        }
        return buildSnippet.length() > 450 ? buildSnippet.substring(0, 450).concat("...")
                : buildSnippet.toString();
    }

    public String substringSearch(String str, List <Lemma> lemmas) {

        String text = "";
        for (Lemma lemma : lemmas) {
            String strLemma = lemma.getLemma();
            if (str.toLowerCase(Locale.ROOT).contains(strLemma)) {
                String replacement = "<b>".concat(strLemma).concat("</b>");
                text = Pattern.compile(strLemma, Pattern.LITERAL |
                         Pattern.CASE_INSENSITIVE |
                         Pattern.UNICODE_CASE).matcher(str)
                         .replaceAll(Matcher.quoteReplacement(replacement));
            }
        }
        int lastIndex = text.indexOf("<b>");
        int nextIndex = text.indexOf("</b>");
        if ((text.length() - nextIndex) > 50) {
            text = text.substring(0, nextIndex + 50).concat("...");
        }
        if (lastIndex > 50) {
           text = text.substring(lastIndex - 50);
            for (char ch : text.toCharArray()) {
                if (Character.isUpperCase(ch)) {
                   int index = text.indexOf(ch);
                   text = text.substring(index);
                   break;
                }
            }
        }
        return text;
    }

    public Map<Page, Float> calculateRelevance(Map<Page, Float> pages) {

        float maxValue = Collections.max(pages.entrySet(),
                Comparator.comparingDouble(Map.Entry::getValue)).getValue();
        pages.forEach((key, value) -> pages.remove(key, value / maxValue));
        return sortByValue(pages);
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {

        Map<K,V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K,V>> stream = map.entrySet().stream();
        stream.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> result.put(e.getKey(),e.getValue()));
        return result;
    }
}
