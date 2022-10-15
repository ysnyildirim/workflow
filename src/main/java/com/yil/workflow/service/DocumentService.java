package com.yil.workflow.service;

import com.yil.workflow.exception.DocumentNotFoundException;
import com.yil.workflow.model.Document;
import com.yil.workflow.repository.DocumentDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final DocumentDao documentDao;

    @Transactional
    public String save(byte[] bytes, Long authenticatedUserId) {
        String hashValue = DigestUtils.md5DigestAsHex(bytes);
        if (!documentDao.existsById(hashValue)) {
            Document document = new Document();
            document.setContent(bytes);
            document.setHashValue(hashValue);
            documentDao.save(document);
        }
        return hashValue;
    }

    @Transactional(readOnly = true)
    public boolean existsById(String hashValue) {
        return documentDao.existsById(hashValue);
    }

    @Transactional(readOnly = true)
    public byte[] findById(String hashValue) throws DocumentNotFoundException {
        return documentDao.findById(hashValue).orElseThrow(DocumentNotFoundException::new).getContent();
    }

}
