package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.response.ResponseService;
import com.example.web_search_engine.response.impl.ErrorResponse;
import com.example.web_search_engine.services.handlers.SearchHandler;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchServiceImplTest {

    @Autowired
    private SearchServiceImpl searchService;

    @MockBean
    private SearchHandler searchHandler;

    private static final String QUERY = "Пример запроса";

    private ResponseService expectedOne;

    private ResponseService expectedTwo;

    @BeforeEach
    public void setUp() {
        ErrorResponse errorResponseOne = new ErrorResponse();
        errorResponseOne.setResult(false);
        errorResponseOne.setError("Не найдено: " + QUERY.toUpperCase(Locale.ROOT));
        expectedOne = errorResponseOne;
        ErrorResponse errorResponseTwo = new ErrorResponse();
        errorResponseTwo.setResult(false);
        errorResponseTwo.setError("Введен пустой запрос");
        expectedTwo = errorResponseTwo;
    }

    @Test
    void searchContent() {
        ResponseService actualOne = searchService.searchContent(QUERY, "SITE_URL", 0, 10);
        ResponseService actualTwo = searchService.searchContent("", "SITE_URL", 0, 10);
        Assert.assertEquals(expectedOne, actualOne);
        Assert.assertEquals(expectedTwo, actualTwo);
        Mockito.verify(searchHandler, Mockito.times(1)).searchData(null, QUERY, 0, 10);
    }
}