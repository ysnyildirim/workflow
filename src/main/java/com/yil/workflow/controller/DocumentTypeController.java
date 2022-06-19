package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.dto.CreateDocumentTypeDto;
import com.yil.workflow.dto.DocumentTypeDto;
import com.yil.workflow.model.DocumentType;
import com.yil.workflow.service.DocumentTypeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "v1/document-types")
public class DocumentTypeController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final DocumentTypeService documentTypeService;

    @Autowired
    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentTypeDto>> findAll() {
        try {
            List<DocumentType> data = documentTypeService.findAllByDeletedTimeIsNull();
            List<DocumentTypeDto> dtoData = new ArrayList<>();
            data.forEach(f -> {
                dtoData.add(DocumentTypeService.toDto(f));
            });
            return ResponseEntity.ok(dtoData);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<DocumentTypeDto> findById(@PathVariable Long id) {
        try {
            DocumentType documentType = documentTypeService.findById(id);
            DocumentTypeDto dto = DocumentTypeService.toDto(documentType);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedDocumentId,
                                 @Valid @RequestBody CreateDocumentTypeDto dto) {
        try {
            DocumentType documentType = new DocumentType();
            documentType.setName(dto.getName());
            documentType.setCreatedUserId(authenticatedDocumentId);
            documentType.setCreatedTime(new Date());
            documentType = documentTypeService.save(documentType);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedDocumentId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateDocumentTypeDto dto) {
        try {
            DocumentType documentType;
            try {
                documentType = documentTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            documentType.setName(dto.getName());
            documentType = documentTypeService.save(documentType);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedDocumentId,
                                         @PathVariable Long id) {
        try {
            DocumentType documentType;
            try {
                documentType = documentTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            documentType.setDeletedUserId(authenticatedDocumentId);
            documentType.setDeletedTime(new Date());
            documentTypeService.save(documentType);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
