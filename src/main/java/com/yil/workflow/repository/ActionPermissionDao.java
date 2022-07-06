/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.repository;

import com.yil.workflow.model.ActionPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionPermissionDao extends JpaRepository<ActionPermission, Long> {

    boolean existsByActionIdAndTargetTypeIdAndUserId(long actionId, int targetTypeId, long userId);

    boolean existsByActionIdAndTargetTypeIdAndGroupId(long actionId, int targetTypeId, long groupId);

    boolean existsByActionIdAndTargetTypeId(long actionId, int targetTypeId);


    @Query(nativeQuery = true,
            value = """
                        select case when count(1) > 0  then true else false end from WFS.ACTION_PERMISSION ap
                        where ap.TARGET_TYPE_ID=3
                        and exists(select 1 from WFS.GROUP_USER gu
                                    where gu.GROUP_ID=ap.GROUP_ID
                                    and gu.USER_ID=:userId)
                    """)
    boolean availableActionInUserId(long actionId, long userId);

}
