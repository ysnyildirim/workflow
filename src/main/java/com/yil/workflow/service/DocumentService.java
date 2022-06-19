package com.yil.workflow.service;

import com.yil.workflow.dto.DocumentDto;
import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document findByIdAndDeletedTimeIsNull(Long id) throws DocumentNotFoundException {
        return documentRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(()-> new DocumentNotFoundException());
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Page<Document> findAllByDeletedTimeIsNull(Pageable pageable) {
        return documentRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
