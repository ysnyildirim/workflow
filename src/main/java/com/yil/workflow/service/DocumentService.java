package com.yil.workflow.service;

import com.yil.workflow.dto.DocumentDto;
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

    public static DocumentDto toDto(Document document) throws NullPointerException {
        if (document == null)
            throw new NullPointerException("Document is null");
        DocumentDto dto = new DocumentDto();
        dto.setId(document.getId());
        dto.setDocumentTypeId(document.getDocumentTypeId());
        dto.setContent(document.getContent());
        dto.setExtension(document.getExtension());
        dto.setUploadedDate(document.getUploadedDate());
        dto.setName(document.getName());
        return dto;
    }

    public Document findById(Long id) throws EntityNotFoundException {
        return documentRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Page<Document> findAllByDeletedTimeIsNull(Pageable pageable) {
        return documentRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
