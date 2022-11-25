package com.example.web_search_engine.repositories;

import com.example.web_search_engine.model.Lemma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LemmaRepository extends JpaRepository<Lemma, Long> {

    @Query(value = "SELECT count(*) from `lemma` WHERE site_id = :id", nativeQuery = true)
    Long findLemmaCountBySiteId(@Param("id") Long id);

    @Query(value = "SELECT * from `lemma` WHERE site_id = :id", nativeQuery = true)
    List<Lemma> findLemmaBySiteId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE from `lemma` WHERE site_id = :siteId", nativeQuery = true)
    void deleteAllBySiteId(@Param("siteId") Long siteId);

    List<Lemma> findByLemma(String lem);
}
