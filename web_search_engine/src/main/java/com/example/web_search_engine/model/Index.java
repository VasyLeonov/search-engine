package com.example.web_search_engine.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "`index`", indexes = {
        @javax.persistence.Index(name = "idx_index_lemma_id", columnList = "lemma_id")
})
@Getter
@Setter
@ToString
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_id")
    private Long pageId;

    @Column(name = "lemma_id")
    private Long lemmaId;

    @Column(name = "`rank`")
    private float rank;
}
