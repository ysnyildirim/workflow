package com.yil.workflow.repository;

import com.yil.workflow.model.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    Page<Priority> findAllByDeletedTimeIsNull(Pageable pageable);

    List<Priority> findAllByNameAndDeletedTimeIsNull(String name);

    Optional<Priority> findByIdAndDeletedTimeIsNull(Long id);

    boolean existsAllByNameAndDeletedTimeIsNull(String name);
}
