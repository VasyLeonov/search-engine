package com.example.web_search_engine.services.impl;

import com.example.web_search_engine.model.Field;
import com.example.web_search_engine.repositories.FieldRepository;
import com.example.web_search_engine.services.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public List<Field> putAndGetFields() {

        List<Field> fields = fieldRepository.findAll();

        if (fields.isEmpty()) {

            Field fieldTitle = new Field();
            Field fieldBody = new Field();

            fieldTitle.setName("title");
            fieldTitle.setSelector("title");
            fieldTitle.setWeight(1.0F);

            fieldBody.setName("body");
            fieldBody.setSelector("body");
            fieldBody.setWeight(0.8F);

            fields.add(fieldRepository.saveAndFlush(fieldTitle));
            fields.add(fieldRepository.saveAndFlush(fieldBody));
        }
        return new ArrayList<>(fields);
    }
}
