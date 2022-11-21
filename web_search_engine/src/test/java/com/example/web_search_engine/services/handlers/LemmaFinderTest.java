package com.example.web_search_engine.services.handlers;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class LemmaFinderTest {

    private LemmaFinder lemmaFinder;

    private static final String TEXT = "Равным образом постоянный количественный рост активности и сфера нашей " +
            "активности способствует повышению актуальности " +
            "существующих финансовых и административных условий";

    private static final String[] EXPECTED = {"количественный", "рост", "наш", "активность", "административный",
            "образ", "постоянный", "способствовать", "сфера", "нашить", "существовать",
            "повышение", "финансовый", "условие", "равный", "актуальность", "существующий"};

    @BeforeEach
    public void setUp() throws IOException {
        lemmaFinder = LemmaFinder.getInstance();
    }

    @Test
    void collectLemmas() {
        Map<String, Integer> actual = lemmaFinder.collectLemmas(TEXT);
        MatcherAssert.assertThat(actual, Matchers.hasEntry("рост", 1));
        MatcherAssert.assertThat(actual, Matchers.hasEntry("активность", 2));
        MatcherAssert.assertThat(actual, Matchers.hasEntry("административный", 1));
        MatcherAssert.assertThat(actual, Matchers.hasEntry("финансовый", 1));
    }

    @Test
    void getLemmaSet() {
        Set<String> actual = lemmaFinder.getLemmaSet(TEXT);
        assertArrayEquals(EXPECTED, actual.toArray());
    }
}