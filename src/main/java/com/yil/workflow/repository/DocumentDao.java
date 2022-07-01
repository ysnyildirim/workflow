package com.yil.workflow.repository;

import com.yil.workflow.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDao extends JpaRepository<Document, Long> {

}
