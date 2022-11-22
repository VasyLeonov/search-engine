package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.Field;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FieldServiceImplTest {

    @Autowired
    private FieldServiceImpl fieldService;

    private List<Field> expected;

    @BeforeEach
    public void setUp(){
        expected = new ArrayList<>();
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
        expected.add(fieldTitle);
        expected.add(fieldBody);
    }

    @Test
    void putAndGetFields() {
        List<Field> actual = fieldService.putAndGetFields();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }
}