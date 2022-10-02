package com.yil.workflow.repository;

import com.yil.workflow.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusDao extends JpaRepository<Status, Integer> {
}
