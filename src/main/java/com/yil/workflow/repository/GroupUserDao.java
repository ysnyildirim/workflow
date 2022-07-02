/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserDao extends JpaRepository<GroupUser, Long> {

    long countByGroupId(long groupId);

    void deleteByGroupId(long groupId);

    boolean existsByGroupIdAndUserIdAndGroupUserTypeId(long groupId, long userId, int groupUserTypeId);

    List<GroupUser> findAllByGroupIdAndUserIdAndGroupUserTypeId(long groupId, long userId, int groupUserTypeId);

}
