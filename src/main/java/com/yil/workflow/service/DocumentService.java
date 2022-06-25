package com.yil.workflow.service;

import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document findByIdAndDeletedTimeIsNull(Long id) throws DocumentNotFoundException {
        return documentRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(()-> new DocumentNotFoundException());
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

}
