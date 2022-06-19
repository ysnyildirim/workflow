package com.yil.workflow.service;

import com.yil.workflow.dto.MessageTypeDto;
import com.yil.workflow.model.MessageType;
import com.yil.workflow.repository.MessageTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MessageTypeService {

    private final MessageTypeRepository messageTypeRepository;

    @Autowired
    public MessageTypeService(MessageTypeRepository messageTypeRepository) {
        this.messageTypeRepository = messageTypeRepository;
    }

    public static MessageTypeDto toDto(MessageType f) {
        if (f == null)
            throw new NullPointerException("Message type is null");
        MessageTypeDto dto = new MessageTypeDto();
        dto.setName(f.getName());
        dto.setId(f.getId());
        return dto;
    }

    public MessageType save(MessageType messageType) {
        return messageTypeRepository.save(messageType);
    }

    public MessageType findById(Long id) throws EntityNotFoundException {
        return messageTypeRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
    }

    public List<MessageType> findAllByDeletedTimeIsNull() {
        return messageTypeRepository.findAllByDeletedTimeIsNull();
    }

}
