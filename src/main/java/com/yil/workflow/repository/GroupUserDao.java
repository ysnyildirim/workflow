/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserDao extends JpaRepository<GroupUser, GroupUser.Pk> {

    void deleteById_GroupId(long groupId);

    boolean existsByIdIn(List<GroupUser.Pk> id);

    long countById_GroupId(long groupId);

}
