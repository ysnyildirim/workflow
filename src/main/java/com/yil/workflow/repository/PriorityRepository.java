package com.yil.workflow.repository;

import com.yil.workflow.model.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    Page<Priority> findAllByDeletedTimeIsNull(Pageable pageable);

    List<Priority> findAllByNameAndDeletedTimeIsNull(String name);

    boolean existsAllByNameAndDeletedTimeIsNull(String name);
}
