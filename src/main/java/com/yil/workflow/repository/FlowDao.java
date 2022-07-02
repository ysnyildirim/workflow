package com.yil.workflow.repository;

import com.yil.workflow.model.Flow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowDao extends JpaRepository<Flow, Long> {

    List<Flow> findAllByDeletedTimeIsNull();

    Optional<Flow> findByIdAndDeletedTimeIsNull(Long id);

    Optional<Flow> findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    @Query(nativeQuery = true,
            value = " select * from wfs.flow f" +
                    "   where f.ENABLED = 1" +
                    "   and f.DELETED_TIME IS NULL" +
                    "   and exists(" +
                    "       select 1 from WFS.STEP s" +
                    "       where s.FLOW_ID=f.ID" +
                    "       and s.ENABLED=1" +
                    "       and s.DELETED_TIME IS null" +
                    "       and s.STEP_TYPE_ID=1" +
                    "       and exists(" +
                    "           select 1 from WFS.ACTION a" +
                    "           where a.STEP_ID=s.ID" +
                    "           and a.ENABLED=1" +
                    "           and a.DELETED_TIME IS NULL" +
                    "           and exists(" +
                    "               select 1 from WFS.ACTION_SOURCE ats" +
                    "               where ats.ACTION_ID=a.ID" +
                    "               and ats.TARGET_TYPE_ID=3" +
                    "               and exists(" +
                    "                   select 1 from WFS.GROUP_USER gu" +
                    "                   where gu.GROUP_ID=ats.GROUP_ID" +
                    "                   and gu.USER_ID=:userId))))")
    List<Flow> getStartUpFlows(@Param(value = "userId") long userId);

}
