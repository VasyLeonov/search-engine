package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.Lemma;
import com.example.web_search_engine.model.WebSite;
import com.example.web_search_engine.services.impl.LemmaServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchHandlerTest {

    @Autowired
    private SearchHandler searchHandler;

    @Autowired
    private LemmaServiceImpl lemmaService;

    private List<Lemma> expectedLemmas;

    private WebSite webSite;


    private static final String TEXT = "Равным образом постоянный " +
            "количественный рост активности и сфера нашей " +
            "активности способствует повышению актуальности " +
            "существующих финансовых и административных условий";

    private static final String[] ARRAY_LEMMAS = {"количественный", "рост", "наш", "активность",
            "административный", "образ", "постоянный", "способствовать", "сфера", "нашить", "существовать",
            "повышение", "финансовый", "условие", "равный", "актуальность", "существующий"};

    private static final String EXPECTED_TEXT = "Равным образом постоянный <b>количественный</b>" +
            " <b>рост</b> активности и сфера нашей активнос... ";

    private static final String TEXT_SHORT = "Равным образом постоянный количественный " +
            "рост активности и сфера нашей активности";

    @BeforeEach
    public void setUp() {
        webSite = null;
        expectedLemmas = new ArrayList<>();
        long id = 0;
        for (String str : ARRAY_LEMMAS) {
            id++;
            Lemma lemma = new Lemma();
            lemma.setId(id);
            lemma.setLemma(str);
            lemma.setFrequency(1);
            lemma.setSiteId(1L);
            lemmaService.putLemma(lemma);
            expectedLemmas.add(lemma);
        }
    }

    @Test
    void findLemmasFromRequest() {
        List<Lemma> actual = searchHandler.findLemmasFromRequest(TEXT, webSite);
        Assert.assertArrayEquals(expectedLemmas.toArray(), actual.toArray());
    }

    @Test
    void substringSearch() {
        Set<String> strLemmas = new HashSet<>();
        strLemmas.add(expectedLemmas.get(0).getLemma());
        strLemmas.add(expectedLemmas.get(1).getLemma());
        String actual = searchHandler.substringSearch(TEXT_SHORT, strLemmas);
        Assertions.assertEquals(EXPECTED_TEXT, actual);
    }
}