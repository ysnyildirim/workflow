package com.yil.workflow.repository;

import com.yil.workflow.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Page<Status> findAllByDeletedTimeIsNull(Pageable pageable);

    List<Status> findAllByNameAndDeletedTimeIsNull(String name);

    Optional<Status> findByIdAndDeletedTimeIsNull(Long id);

    boolean existsAllByNameAndDeletedTimeIsNull(String name);
}
