package com.yil.workflow.repository;

import com.yil.workflow.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllByDeletedTimeIsNull(Pageable pageable);
}
