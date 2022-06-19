package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
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
@RequestMapping(value = "/api/wf/v1/messages")
public class MessageController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<PageDto<MessageDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Message> messagePage = messageService.findAllByDeletedTimeIsNull(pageable);
            PageDto<MessageDto> pageDto = PageDto.toDto(messagePage, MessageService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<MessageDto> findById(@PathVariable Long id) {
        try {
            Message message;
            try {
                message = messageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            MessageDto dto = MessageService.toDto(message);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedMessageId,
                                 @Valid @RequestBody CreateMessageDto dto) {
        try {
            Message message = new Message();
            message.setTitle(dto.getTitle());
            message.setContent(dto.getContent());
            message.setCreatedUserId(authenticatedMessageId);
            message.setCreatedTime(new Date());
            message = messageService.save(message);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedMessageId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateMessageDto dto) {
        try {
            Message message = null;
            try {
                message = messageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            }
            message.setTitle(dto.getTitle());
            message.setContent(dto.getContent());
            message.setCreatedUserId(authenticatedMessageId);
            message = messageService.save(message);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedMessageId,
                                         @PathVariable Long id) {
        try {
            Message message;
            try {
                message = messageService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            message.setDeletedUserId(authenticatedMessageId);
            message.setDeletedTime(new Date());
            messageService.save(message);
            return ResponseEntity.ok("Message deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


}
