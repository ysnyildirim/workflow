package com.yil.workflow.service;

import com.yil.workflow.dto.MessageDto;
import com.yil.workflow.model.Message;
import com.yil.workflow.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public static MessageDto toDto(Message message) throws NullPointerException {
        if (message == null)
            throw new NullPointerException("Message is null");
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setMessageTypeId(message.getMessageTypeId());
        dto.setContent(message.getContent());
        dto.setTitle(message.getTitle());
        return dto;
    }

    public Message findById(Long id) throws EntityNotFoundException {
        return messageRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Page<Message> findAllByDeletedTimeIsNull(Pageable pageable) {
        return messageRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
