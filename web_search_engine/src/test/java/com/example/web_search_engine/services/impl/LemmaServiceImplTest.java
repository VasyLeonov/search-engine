package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.Lemma;
import com.example.web_search_engine.model.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LemmaServiceImplTest {

    @Autowired
    private LemmaServiceImpl lemmaService;

    private List<Page> pages;

    private static final String CONTENT = "<p>Равным образом постоянный " +
            "количественный рост активности и сфера нашей " +
            "активности способствует повышению актуальности " +
            "существующих финансовых и административных условий</p>";

    private static final String[] EXPECTED = {"количественный", "рост", "наш", "активность", "административный",
            "образ", "постоянный", "способствовать", "сфера", "нашить", "существовать",
            "повышение", "финансовый", "условие", "равный", "актуальность", "существующий"};


    @BeforeEach
    public void setUp() {
        pages = new ArrayList<>();
        Page page = new Page();
        page.setSiteId(1L);
        page.setCode(200);
        page.setContent(CONTENT);
        page.setPath("/example");
        pages.add(page);
    }

    @Test
    void createLemmas() {
        lemmaService.createLemmas(pages);
        List<Lemma> lemmas = lemmaService.getAllLemmasBySiteId(1L);
        for (String str : EXPECTED) {
            boolean actual = lemmas.stream().anyMatch(l -> l.getLemma().equals(str));
            assertTrue(actual);
        }
    }
}