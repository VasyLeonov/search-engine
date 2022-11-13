package com.example.web_search_engine.repositories;

import com.example.web_search_engine.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}