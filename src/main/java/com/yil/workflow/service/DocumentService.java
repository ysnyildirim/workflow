package com.yil.workflow.service;

import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document findByIdAndDeletedTimeIsNull(Long id) throws DocumentNotFoundException {
        return documentRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new DocumentNotFoundException());
    }

    @Transactional
    public Document save(Document document) {
        return documentRepository.save(document);
    }

}
