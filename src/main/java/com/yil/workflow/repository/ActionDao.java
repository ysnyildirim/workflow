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
            value = """
                        select case when count(1)>0 then true else false end from WFS.ACTION a
                        where a.Id=:id
                        and a.TARGET_TypE_ID=3
                        and exists
                        (
                            select 1 from WFS.GROUP_USER gu
                            where gu.ID=a.GROUP_ID
                            and gu.GROUP_USER_TYPE_ID IN(1,2)
                            and gu.USER_ID=:userId
                        )
                    """)
    boolean taskCanChangedByActionIdAndUserId(@Param(value = "id") long id,
                                              @Param(value = "userId") long userId);

    List<Action> findAllByStepIdAndTargetTypeIdAndEnabledTrueAndDeletedTimeIsNull(long stepId,
                                                                                  int targetTypeId);

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
    boolean isNextAction(@Param(value = "currentActionId") long currentActionId,
                         @Param(value = "nextActionId") long nextActionId);

    @Query(nativeQuery = true,
            value = """
                        with tbl as(
                        select * from WFS.ACTION a
                        where a.ENABLED=1
                        and a.DELETED_TIME IS NULL
                        and a.TARGET_TYPE_ID=4
                        and a.USER_ID=:userId
                        union all 
                        select * from WFS.ACTION a
                        where a.ENABLED=1
                        and a.DELETED_TIME IS NULL
                        and a.TARGET_TYPE_ID=3
                        and exists(
                            select 1 from WFS.GROUP_USER gu
                            where gu.GROUP_ID=a.GROUP_ID
                            and gu.USER_ID=:userId
                            and gu.GROUP_USER_TYPE_ID=3))
                        select * from tbl t
                        where exists(
                            select 1 from WFS.STEP s
                            where s.ID=t.STEP_ID 
                            and s.DELETED_TIME IS NULL
                            and s.ENABLED=1
                            and exists (
                                select 1 from WFS.FLOW f
                                where f.ID=s.FLOW_ID
                                and f.ID=:flowId
                                and f.ENABLED=1
                                and f.DELETED_TIME IS NULL))
                        
                    """)
    List<Action> getStartUpActions(@Param(value = "flowId") long flowId,
                                   @Param(value = "userId") long userId);

    @Query(nativeQuery = true,
            value = """
                        select * from WFS.ACTION a
                        where a.ENABLED=1
                        and a.DELETED_TIME IS NULL
                        and a.STEP_ID=:stepId
                        and a.TARGET_TYPE_ID =3
                        and exists(
                            select 1 from WFS.GROUP_USER gu
                            where gu.GROUP_ID=a.GROUP_ID
                            and gu.USER_ID=:userId
                            and gu.GROUP_USER_TYPE_ID=3)
                        and exists(
                            select 1 from WFS.STEP s
                            where s.ID=a.STEP_ID 
                            and s.DELETED_TIME IS NULL
                            and s.ENABLED=1
                            and exists (
                                select 1 from WFS.FLOW f
                                where f.ID=s.FLOW_ID
                                and f.ENABLED=1
                                and f.DELETED_TIME IS NULL))
                    """)
    List<Action> getGroupActionsByStepIdAndUserId(@Param(value = "stepId") long stepId,
                                                  @Param(value = "userId") long userId);
}
