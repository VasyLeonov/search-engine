package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.*;
import com.example.web_search_engine.services.impl.IndexServiceImpl;
import com.example.web_search_engine.services.impl.LemmaServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class IndexHandlerTest {

    @Autowired
    private IndexHandler indexHandler;

    @Autowired
    private LemmaServiceImpl lemmaService;

    @Autowired
    private IndexServiceImpl indexService;

    private WebSite webSite;

    private List<Page> pages;

    private List<Field> fields;

    private List<Lemma> lemmas;

    private static final String CONTENT = "<p>Равным образом постоянный " +
            "количественный рост и сфера нашей " +
            "активности способствует повышению актуальности " +
            "существующих финансовых и административных условий</p>";

    private static final String[] STR_LEMMAS = {"количественный", "рост", "наш", "активность", "административный",
            "образ", "постоянный", "способствовать", "сфера", "нашить", "существовать",
            "повышение", "финансовый", "условие", "равный", "актуальность", "существующий"};


    @BeforeEach
    public void setUp() {
        pages = new ArrayList<>();
        fields = new ArrayList<>();
        lemmas = new ArrayList<>();
        webSite = new WebSite();
        webSite.setId(1L);
        webSite.setStatusTime(LocalDateTime.now());
        webSite.setStatus(Status.INDEXING);
        webSite.setUrl("http://example.ru");
        webSite.setName("Example");
        addFields();
        addPage();
        addLemmas();
    }

    public void addLemmas() {
        for (String str : STR_LEMMAS) {
            Lemma lemma = new Lemma();
            lemma.setLemma(str);
            lemma.setFrequency(1);
            lemma.setSiteId(webSite.getId());
            lemmas.add(lemmaService.putLemma(lemma));
        }
    }

    public void addPage() {
        Page page = new Page();
        page.setId(1L);
        page.setPath("/example");
        page.setContent(CONTENT);
        page.setSiteId(webSite.getId());
        page.setCode(200);
        pages.add(page);
    }

    public void addFields() {
        Field fieldTitle = new Field();
        Field fieldBody = new Field();

        fieldTitle.setId(1L);
        fieldTitle.setName("title");
        fieldTitle.setSelector("title");
        fieldTitle.setWeight(1.0F);

        fieldBody.setId(2L);
        fieldBody.setName("body");
        fieldBody.setSelector("body");
        fieldBody.setWeight(0.8F);

        fields.add(fieldTitle);
        fields.add(fieldBody);
    }

    @Test
    void createIndexes() {
        indexHandler.createIndexes(fields, pages, webSite);
        for (Lemma lemma : lemmas) {
            List<Index> indexes = indexService.getIndexesByLemmaId(lemma.getId());
            Assert.assertNotNull(indexes.get(0));
        }
        int expected = lemmas.size();
        int actual = Math.toIntExact(indexService.countIndexes());
        Assert.assertEquals(expected, actual);
    }

    @Test
    void calculateRank() {
        float actual1 = indexHandler.calculateRank(fields, 0, 4);
        float expected1 = 3.2f;

        float actual2 = indexHandler.calculateRank(fields, 1, 4);
        float expected2 = 4.2f;

        float actual3 = indexHandler.calculateRank(fields, 5, 0);
        float expected3 = 5f;

        float actual4 = indexHandler.calculateRank(fields, 0, 0);
        float expected4 = 0f;

        Assert.assertEquals(expected1, actual1, expected1);
        Assert.assertEquals(expected2, actual2, expected2);
        Assert.assertEquals(expected3, actual3, expected3);
        Assert.assertEquals(expected4, actual4, expected4);
    }
}