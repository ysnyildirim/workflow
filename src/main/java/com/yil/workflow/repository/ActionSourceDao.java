/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActionSourceDao extends JpaRepository<ActionSource, Long> {


    List<ActionSource> findAllByActionIdAndTargetTypeId(long actionId, int targetTypeId);

    List<ActionSource> findAllByActionId(long actionId);

    Optional<ActionSource> findByActionIdAndGroupIdAndTargetTypeId(Long actionId, Long groupId, Integer targetTypeId);

    boolean existsByActionIdAndTargetTypeId(long actionId, int targetTypeId);

    @Query(nativeQuery = true,
            value = " select case when count(1) > 0  then true else false end " +
                    " from WFS.ACTION_SOURCE s " +
                    "   where s.TARGET_TYPE_ID=3" +
                    "   and s.ACTION_ID=:actionId" +
                    "   and exists (select 1 from WFS.GROUP_USER gu where s.GROUP_ID= gu.GROUP_ID and gu.USER_ID=:userId)")
    boolean userInActionGroup(@Param(value = "actionId") long actionId, @Param(value = "userId") long userId);

}
