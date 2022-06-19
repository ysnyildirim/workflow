package com.yil.workflow.controller;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.dto.CreateMessageTypeDto;
import com.yil.workflow.dto.MessageTypeDto;
import com.yil.workflow.model.MessageType;
import com.yil.workflow.service.MessageTypeService;
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
@RequestMapping(value = "v1/message-types")
public class MessageTypeController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final MessageTypeService messageTypeService;

    @Autowired
    public MessageTypeController(MessageTypeService messageTypeService) {
        this.messageTypeService = messageTypeService;
    }

    @GetMapping
    public ResponseEntity<List<MessageTypeDto>> findAll() {
        try {
            List<MessageType> data = messageTypeService.findAllByDeletedTimeIsNull();
            List<MessageTypeDto> dtoData = new ArrayList<>();
            data.forEach(f -> {
                dtoData.add(MessageTypeService.toDto(f));
            });
            return ResponseEntity.ok(dtoData);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<MessageTypeDto> findById(@PathVariable Long id) {
        try {
            MessageType messageType = messageTypeService.findById(id);
            MessageTypeDto dto = MessageTypeService.toDto(messageType);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedMessageId,
                                 @Valid @RequestBody CreateMessageTypeDto dto) {
        try {
            MessageType messageType = new MessageType();
            messageType.setName(dto.getName());
            messageType.setCreatedUserId(authenticatedMessageId);
            messageType.setCreatedTime(new Date());
            messageType = messageTypeService.save(messageType);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedMessageId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateMessageTypeDto dto) {
        try {
            MessageType messageType;
            try {
                messageType = messageTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            messageType.setName(dto.getName());
            messageType = messageTypeService.save(messageType);
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
            MessageType messageType;
            try {
                messageType = messageTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            messageType.setDeletedUserId(authenticatedMessageId);
            messageType.setDeletedTime(new Date());
            messageTypeService.save(messageType);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
