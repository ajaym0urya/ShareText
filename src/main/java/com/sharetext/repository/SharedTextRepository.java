package com.sharetext.repository;

import com.sharetext.model.SharedText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedTextRepository extends JpaRepository<SharedText, String> {
}
