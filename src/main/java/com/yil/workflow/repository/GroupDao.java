/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    Optional<Group> findByIdAndDeletedTimeIsNull(Long id);

}
