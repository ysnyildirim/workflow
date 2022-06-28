package com.yil.workflow.repository;

import com.yil.workflow.model.PriorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PriorityTypeDao extends JpaRepository<PriorityType, Integer> {

}
