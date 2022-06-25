/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionUserDao extends JpaRepository<ActionUser, ActionUser.Pk> {

    List<ActionUser> findById_UserId(long userId);

}
