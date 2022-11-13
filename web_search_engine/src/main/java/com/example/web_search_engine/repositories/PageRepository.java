package com.example.web_search_engine.repositories;

import com.example.web_search_engine.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    @Query(value = "SELECT * from `page` WHERE site_id = :id", nativeQuery = true)
    List<Page> findPagesBySiteId(@Param("id") Long id);

    @Query(value = "SELECT count(*) from `page` WHERE site_id = :id", nativeQuery = true)
    Long findPageCountBySiteId(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE from `page` WHERE site_id = :siteId", nativeQuery = true)
    void deleteAllBySiteId(@Param("siteId") Long siteId);

    @Modifying
    @Query(value = "DELETE from `page` WHERE id = :pageId AND site_id = :siteId", nativeQuery = true)
    Optional<Page> getBySiteIdAndPageId(@Param("siteId") Long siteId, @Param("pageId") Long pageId);
}
