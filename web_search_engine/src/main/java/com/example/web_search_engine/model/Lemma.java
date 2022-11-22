package com.example.web_search_engine.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Index;

@Entity
@Table(name = "lemma", indexes = {@Index(name = "idx_lemma_site_id", columnList = "site_id")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Lemma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lemma;

    private Integer frequency;

    @Column(name = "site_id")
    private Long siteId;

    public Lemma() {
    }
}
