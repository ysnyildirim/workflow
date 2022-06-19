package com.yil.workflow.service;

import com.yil.workflow.dto.DocumentTypeDto;
import com.yil.workflow.model.DocumentType;
import com.yil.workflow.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DocumentTypeService {

    private final DocumentTypeRepository documentTypeRepository;

    @Autowired
    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    public static DocumentTypeDto toDto(DocumentType f) {
        if (f == null)
            throw new NullPointerException("Document type is null");
        DocumentTypeDto dto = new DocumentTypeDto();
        dto.setName(f.getName());
        dto.setId(f.getId());
        return dto;
    }

    public DocumentType save(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    public DocumentType findById(Long id) throws EntityNotFoundException {
        return documentTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
    }

    public List<DocumentType> findAllByDeletedTimeIsNull() {
        return documentTypeRepository.findAllByDeletedTimeIsNull();
    }

}
