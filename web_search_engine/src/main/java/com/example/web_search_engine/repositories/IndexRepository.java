package com.example.web_search_engine.repositories;

import com.example.web_search_engine.model.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndexRepository extends JpaRepository<Index, Long> {

    @Query(value = "SELECT * FROM `index` WHERE lemma_id = :lemmaId", nativeQuery = true)
    List<Index> findIndexByLemmaId(@Param("lemmaId") long lemmaId);

    @Modifying
    @Query(value = "DELETE from `index` WHERE page_id = :pageId", nativeQuery = true)
    void deleteAllBySiteId(@Param("pageId") Long pageId);
}
