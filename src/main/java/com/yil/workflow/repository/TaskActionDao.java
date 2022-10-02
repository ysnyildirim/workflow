package com.yil.workflow.repository;

import com.yil.workflow.model.TaskAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskActionDao extends JpaRepository<TaskAction, Long> {
    Page<TaskAction> findAllByTaskId(Pageable pageable, Long taskId);

    Boolean existsByIdNotAndTaskId(Long id, Long taskId);

    Optional<TaskAction> findByIdAndTaskId(Long id, Long taskId);

    @Query(nativeQuery = true,
            value = " select * from WFS.TASK_ACTION ta " +
                    " where ta.TASK_ID=:taskId" +
                    " and ta.Id = (select max(ta2.Id) from WFS.TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)")
    Optional<TaskAction> getLastAction(@Param(value = "taskId") long taskId);

    boolean existsByIdAndCreatedUserId(long id, long createdUserId);

    @Query(nativeQuery = true,
            value = " select * from WFS.TASK_ACTION ta " +
                    " where ta.TASK_ID=:taskId" +
                    " and ta.Id = (select min(ta2.Id) from WFS.TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)")
    Optional<TaskAction> getFirstAction(@Param(value = "taskId") long taskId);

    @Query(nativeQuery = true,
            value = """
                    SELECT case when (count(1) > 0)  then true else false end
                    FROM WFS.TASK_ACTION TA
                    WHERE TA.CREATED_USER_ID =:userId
                    	AND TA.TASK_ID =:taskId
                    	AND TA.ID =
                    		(SELECT min(TA2.ID)
                    			FROM WFS.TASK_ACTION TA2
                    			WHERE TA.TASK_ID = TA2.TASK_ID)
                                        """)
    boolean isTaskCreatedUser(@Param(value = "taskId") long taskId, @Param(value = "userId") long userId);

    boolean existsByTaskIdAndCreatedUserId(long taskId, long userId);

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM WFS.TASK_ACTION TA
                    WHERE TA.ID =
                    		(SELECT MAX(ID)
                    			FROM WFS.TASK_ACTION TA2
                    			WHERE TA2.TASK_ID = :taskId
                    				AND TA2.CREATED_USER_ID <> :userId)
                    """)
    Optional<TaskAction> getActionCreatedLastDifferentUser(@Param(value = "taskId") long taskId, @Param(value = "userId") long userId);
}
