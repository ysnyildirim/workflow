package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.CreateDocumentDto;
import com.yil.workflow.dto.DocumentDto;
import com.yil.workflow.model.Document;
import com.yil.workflow.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/wf/v1/documents")
public class DocumentController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<PageDto<DocumentDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Document> documentPage = documentService.findAllByDeletedTimeIsNull(pageable);
            PageDto<DocumentDto> pageDto = PageDto.toDto(documentPage, DocumentService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<DocumentDto> findById(@PathVariable Long id) {
        try {
            Document document;
            try {
                document = documentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            DocumentDto dto = DocumentService.toDto(document);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedDocumentId,
                                 @Valid @RequestBody CreateDocumentDto dto) {
        try {
            Document document = new Document();
            document.setName(dto.getName());
            document.setContent(dto.getContent());
            document.setExtension(dto.getExtension());
            document.setUploadedDate(dto.getUploadedDate());
            document.setCreatedUserId(authenticatedDocumentId);
            document.setCreatedTime(new Date());
            document = documentService.save(document);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedDocumentId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateDocumentDto dto) {
        try {
            Document document = null;
            try {
                document = documentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            document.setName(dto.getName());
            document.setContent(dto.getContent());
            document.setExtension(dto.getExtension());
            document.setUploadedDate(dto.getUploadedDate());
            document = documentService.save(document);
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
            Document document;
            try {
                document = documentService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            document.setDeletedUserId(authenticatedDocumentId);
            document.setDeletedTime(new Date());
            documentService.save(document);
            return ResponseEntity.ok("Document deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
