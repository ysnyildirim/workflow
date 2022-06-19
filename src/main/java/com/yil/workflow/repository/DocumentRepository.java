package com.yil.workflow.repository;

import com.yil.workflow.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findAllByDeletedTimeIsNull(Pageable pageable);

    Optional<Document> findByIdAndDeletedTimeIsNull(Long id);
}
