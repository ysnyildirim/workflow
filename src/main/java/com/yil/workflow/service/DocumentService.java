package com.yil.workflow.service;

import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.repository.DocumentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final DocumentDao documentDao;

    @Transactional(readOnly = true)
    public Document findById(Long id) throws DocumentNotFoundException {
        return documentDao.findById(id).orElseThrow(() -> new DocumentNotFoundException());
    }

    @Transactional(rollbackFor = {Throwable.class})
    public Document save(Document document) {
        return documentDao.save(document);
    }

}
