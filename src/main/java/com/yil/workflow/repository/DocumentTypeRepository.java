package com.yil.workflow.repository;

import com.yil.workflow.model.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
    List<DocumentType> findAllByDeletedTimeIsNull();
}
