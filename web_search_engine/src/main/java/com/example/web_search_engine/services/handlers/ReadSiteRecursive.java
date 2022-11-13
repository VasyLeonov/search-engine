package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.Page;
import com.example.web_search_engine.model.Status;
import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.services.impl.SiteServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;

public class ReadSiteRecursive extends RecursiveAction {
    private final String url;

    private final WebSite site;

    private final String userAgent;

    private final SiteServiceImpl siteService;

    private static final List<String> urlsList = new CopyOnWriteArrayList<>();

    private static final List<Page> pageList = new CopyOnWriteArrayList<>();

    public ReadSiteRecursive(String url, WebSite site, String userAgent, SiteServiceImpl siteService) {
        this.userAgent = userAgent;
        this.siteService = siteService;
        this.site = site;
        this.url = url;
    }

    @Override
    protected void compute() {

        List<ReadSiteRecursive> readSiteList = new ArrayList<>();
        try {
            Document document = getDocument(url);
            Elements elements = document.select("html").select("a");

            elements.forEach(element -> {
                String absHref = element.attr("abs:href");
                String relHref = element.attr("href");
                if (checkingUrl(url, absHref)) {
                    ReadSiteRecursive readSite = new ReadSiteRecursive(absHref, site, userAgent, siteService);
                    readSite.fork();
                    Page page = new Page();
                    page.setPath(relHref);
                    page.setCode(document.connection().response().statusCode());
                    page.setContent(document.select("html").outerHtml());
                    page.setSiteId(site.getId());
                    pageList.add(page);
                    readSiteList.add(readSite);
                    urlsList.add(absHref);
                }
            });
        } catch (Exception e) {
            site.setLastError(site.getLastError() == null ? "- Ошибка чтения страницы: " + url :
                    site.getLastError().concat("\n - Ошибка чтения страницы: " + url));
            site.setStatus(Status.FAILED);
        }
        readSiteList.forEach(ReadSiteRecursive::join);
    }

    public Document getDocument(String url) throws Exception {

        Thread.sleep(500);
        return Jsoup.connect(url)
                .userAgent(userAgent)
                .referrer("http://www.google.com")
                .get();
    }

    public static List<Page> getPageList() {
        return pageList;
    }

    public boolean checkingUrl(String absUrl, String absHref) {
        return !absHref.equals(absUrl) &&
                absHref.startsWith(absUrl) &&
                !urlsList.contains(absHref)
                && !absHref.contains("#");
    }
}
