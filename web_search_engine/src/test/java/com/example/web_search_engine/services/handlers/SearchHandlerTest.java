package com.example.web_search_engine.services.handlers;

import com.example.web_search_engine.model.Lemma;
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
import java.util.List;

/**
 * Test for empty database
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class SearchHandlerTest {

    @Autowired
    private SearchHandler searchHandler;

    @Autowired
    private LemmaServiceImpl lemmaService;

    private List<Lemma> expectedLemmas;


    private static final String TEXT = "Равным образом постоянный " +
            "количественный рост активности и сфера нашей " +
            "активности способствует повышению актуальности " +
            "существующих финансовых и административных условий";

    private static final String[] ARRAY_LEMMAS = {"количественный", "рост", "наш", "активность",
            "административный", "образ", "постоянный", "способствовать", "сфера", "нашить", "существовать",
            "повышение", "финансовый", "условие", "равный", "актуальность", "существующий"};


    private static final String EXPECTED_TEXT = "Равным образом постоянный <b>количественный</b> рост " +
            "активности и сфера нашей активности Равным образом постоянный количественный <b>рост</b> " +
            "активности и сфера нашей активности ";

    private static final String TEXT_SHORT = "Равным образом постоянный количественный " +
            "рост активности и сфера нашей активности";

    @BeforeEach
    public void setUp() {
        expectedLemmas = new ArrayList<>();
        for (String str : ARRAY_LEMMAS) {
            Lemma lemma = new Lemma();
            lemma.setLemma(str);
            lemma.setFrequency(1);
            lemma.setSiteId(1L);
            expectedLemmas.add(lemmaService.putLemma(lemma));
        }
    }

    @Test
    void findLemmasFromRequest() {
        List<Lemma> actual = searchHandler.findLemmasFromRequest(TEXT);
        Assert.assertArrayEquals(expectedLemmas.toArray(), actual.toArray());
    }

    @Test
    void substringSearch() {
        List<Lemma> lemmas = new ArrayList<>();
        lemmas.add(expectedLemmas.get(0));
        lemmas.add(expectedLemmas.get(1));
        String actual = searchHandler.substringSearch(TEXT_SHORT, lemmas);
        Assertions.assertEquals(EXPECTED_TEXT, actual);
    }
}