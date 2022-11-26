package com.example.web_search_engine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.persistence.Index;

@Entity
@Table(name = "page", indexes = {
        @Index(name = "idx_webpage_path", columnList = "path")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Page implements Comparable<Page> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private Integer code;

    @Column(columnDefinition = "MEDIUMTEXT")
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "site_id")
    private Long siteId;


    @Override
    public int compareTo(Page o) {
        return this.getId().compareTo(o.getId());
    }
}
