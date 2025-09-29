package com.ajax.sharetext;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TextRepository extends CrudRepository<Text, Long> {

    Text findTextByUrl(String url);
    
    boolean existsByUrl(String url);

    @Query("SELECT t.id FROM Text t WHERE t.url = :url")
    Long findIdByUrl(@Param("url") String url);
    
    
    @Query("SELECT t.password FROM Text t WHERE t.url = :url")
    String findPasswordByUrl(@Param("url") String url);
    
    @Query("SELECT t.hasPassword FROM Text t WHERE t.url = :url")
    boolean findIfTextHasPasswordByUrl(@Param("url") String url);
}
