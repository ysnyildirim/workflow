package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActionDao extends JpaRepository<Action, Long> {
    List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId);

    Optional<Action> findByIdAndStepId(Long id, Long stepId);

    Optional<Action> findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id);

    Optional<Action> findByIdAndDeletedTimeIsNull(Long id);

    @Query(nativeQuery = true,
            value = " select case when count(1) > 0  then true else false end " +
                    " from WFS.Action a " +
                    "   where a.Enabled=1" +
                    "   and a.Id=:id" +
                    "   and a.DELETED_TIME is null" +
                    "   and exists (" +
                    "       select 1 from WFS.Step s " +
                    "       where  s.ID =a.STEP_ID " +
                    "       and s.enabled=1 " +
                    "       and s.STEP_TYPE_ID=1" +
                    "       and s.DELETED_TIME is null)")
    boolean isStartUpAction(@Param(value = "id") long id);

    @Query(nativeQuery = true,
            value = " select case when count(1) > 0  then true else false end" +
                    " from WFS.ACTION a " +
                    "   where a.ENABLED=1" +
                    "   and a.ID=:nextActionId" +
                    "   and a.DELETED_TIME IS NULL" +
                    "   and EXISTS(" +
                    "       select 1 from WFS.ACTION a2" +
                    "       where a2.ID=:currentActionId" +
                    "       and a2.NEXT_STEP_ID=a.STEP_ID)" +
                    "   and exists(" +
                    "       select 1 from WFS.STEP s" +
                    "       where s.ID=a.STEP_ID" +
                    "       and s.ENABLED=1" +
                    "       and s.DELETED_TIME IS NULL)")
    boolean isNextAction(@Param(value = "currentActionId") long currentActionId, @Param(value = "nextActionId") long nextActionId);

    @Query(nativeQuery = true,
            value = "   select * from WFS.ACTION a" +
                    "   where a.ENABLED=1" +
                    "   and a.DELETED_TIME IS NULL" +
                    "   and exists(" +
                    "       select 1 from WFS.ACTION_SOURCE ats" +
                    "       where ats.ACTION_ID=a.ID" +
                    "       and ats.TARGET_TYPE_ID=3" +
                    "       and exists(" +
                    "           select 1 from WFS.GROUP_USER gu" +
                    "           where gu.GROUP_ID=ats.GROUP_ID" +
                    "           and gu.USER_ID=:userId))" +
                    "   and exists(" +
                    "       select 1 from WFS.STEP s" +
                    "       where s.ID=a.STEP_ID" +
                    "       and s.ENABLED=1" +
                    "       and s.DELETED_TIME IS NULL" +
                    "       and s.ID=:stepId)")
    List<Action> getActionByStepIdAndUserId(@Param(value = "stepId") long stepId,
                                            @Param(value = "userId") long userId);

    @Query(nativeQuery = true,
            value = "   select * from WFS.ACTION a" +
                    "   where a.ENABLED=1" +
                    "   and a.DELETED_TIME IS NULL" +
                    "   and a.STEP_ID=:stepId" +
                    "   and exists(" +
                    "       select  1 from WFS.STEP s" +
                    "       where s.ENABLED=1" +
                    "       and s.ID=a.STEP_ID" +
                    "       and s.DELETED_TIME IS NULL)" +
                    "   and exists(" +
                    "       select 1 from WFS.ACTION_SOURCE ats" +
                    "       where ats.ACTION_ID=a.ID" +
                    "       and ats.TARGET_TYPE_ID=:targetTypeId)")
    List<Action> getActionByStepIdAndTargetTypeId(@Param(value = "stepId") long stepId,
                                                  @Param(value = "targetTypeId") int targetTypeId);

    @Query(nativeQuery = true,
            value = "   select * from WFS.ACTION a" +
                    "   where a.ENABLED=1" +
                    "   and a.DELETED_TIME IS NULL" +
                    "   and exists(" +
                    "       select 1 from WFS.ACTION_SOURCE ats" +
                    "       where ats.ACTION_ID=a.ID" +
                    "       and ats.TARGET_TYPE_ID=3" +
                    "       and exists(" +
                    "           select 1 from WFS.GROUP_USER gu" +
                    "           where gu.GROUP_ID=ats.GROUP_ID" +
                    "           and gu.USER_ID=:userId))" +
                    "   and exists(" +
                    "       select 1 from WFS.STEP s" +
                    "       where s.ID=a.STEP_ID" +
                    "       and s.ENABLED=1" +
                    "       and s.DELETED_TIME IS NULL" +
                    "       and s.STEP_TYPE_ID=1" +
                    "       and exists(" +
                    "           select 1 from WFS.FLOW f" +
                    "           where f.ID=s.FLOW_ID" +
                    "           and f.ENABLED=1" +
                    "           and f.DELETED_TIME IS NULL" +
                    "           and f.ID=:flowId))")
    List<Action> getStartUpActions(@Param(value = "flowId") long flowId,
                                   @Param(value = "userId") long userId);
}
