package com.yil.workflow.repository;

import com.yil.workflow.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
                    "       where  s.id =a.stepId " +
                    "       and s.enabled=1 " +
                    "       and s.STEP_TYPE_ID=1" +
                    "       and s.DELETED_TIME is null)")
    boolean isStartUpAction(long id);

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
    boolean isNextAction(long currentActionId, long nextActionId);
}
