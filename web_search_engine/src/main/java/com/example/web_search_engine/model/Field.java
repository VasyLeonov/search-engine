package com.example.web_search_engine.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;

@Entity
@Table(name = "field")
@Getter
@Setter
@ToString
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String selector;

    private float weight;
}
