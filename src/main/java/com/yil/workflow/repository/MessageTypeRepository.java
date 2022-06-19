package com.yil.workflow.repository;

import com.yil.workflow.model.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageTypeRepository extends JpaRepository<MessageType, Long> {
    List<MessageType> findAllByDeletedTimeIsNull();
}
