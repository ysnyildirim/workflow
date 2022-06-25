package com.yil.workflow.repository;

import com.yil.workflow.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PriorityRepository extends JpaRepository<Priority, Integer> {

}
