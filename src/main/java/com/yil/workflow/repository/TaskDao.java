package com.yil.workflow.repository;

import com.yil.workflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface TaskDao extends JpaRepository<Task, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = """
                        update WFS.TASK t set t.CLOSED=1
                        where t.ID=:taskId
                    """)
    void closedTask(@Param(value = "taskId") long taskId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = """
                        update WFS.TASK t set CLOSED=0
                        where t.ID=:taskId
                    """)
    void openTask(@Param(value = "taskId") long taskId);

    @Query(nativeQuery = true,
            value = """
                    SELECT *
                    FROM WFS.TASK T
                    WHERE T.CLOSED = 0
                    	AND EXISTS
                    		(SELECT 1
                    			FROM WFS.TASK_ACTION TA
                    			WHERE TA.TASK_ID = T.ID
                    				AND TA.ASSIGNED_USER_ID = :userId
                    				AND TA.ID =
                    					(SELECT MAX(TA2.ID)
                    						FROM WFS.TASK_ACTION TA2
                    						WHERE TA2.TASK_ID = TA.TASK_ID))
                                        """,
            countQuery = """
                    SELECT COUNT(T.ID)
                    FROM WFS.TASK T
                    WHERE T.CLOSED = 0
                    	AND EXISTS
                    		(SELECT 1
                    			FROM WFS.TASK_ACTION TA
                    			WHERE TA.TASK_ID = T.ID
                    				AND TA.ASSIGNED_USER_ID = :userId
                    				AND TA.ID =
                    					(SELECT MAX(TA2.ID)
                    						FROM WFS.TASK_ACTION TA2
                    						WHERE TA2.TASK_ID = TA.TASK_ID))
                                        """)
    Page<Task> findAllByAssignedUserId(Pageable pageable, @Param(value = "userId") long userId);

    @Query(nativeQuery = true,
            value = """
                    WITH TBL AS
                    	(SELECT TA.TASK_ID
                    		FROM WFS.TASK_ACTION TA
                    		WHERE TA.CREATED_USER_ID = :userId
                    			AND TA.ID =
                    				(SELECT MIN(TA2.ID)
                    					FROM WFS.TASK_ACTION TA2
                    					WHERE TA2.TASK_ID = TA.TASK_ID))
                    SELECT *
                    FROM WFS.TASK T
                    WHERE T.ID in
                    		(SELECT TASK_ID
                    			FROM TBL)
                                        """,
            countQuery = """
                    SELECT COUNT(1)
                    FROM
                    	(SELECT 1
                    		FROM WFS.TASK_ACTION TA
                    		WHERE TA.CREATED_USER_ID = :userId
                    			AND TA.ID =
                    				(SELECT MIN(TA2.ID)
                    					FROM WFS.TASK_ACTION TA2
                    					WHERE TA2.TASK_ID = TA.TASK_ID)
                    		GROUP BY TA.TASK_ID) t
                                        """)
    Page<Task> findAllByCreatedUserId(Pageable pageable, @Param(value = "userId") long userId);

    @Query(nativeQuery = true,
            value = """
                    WITH TBL AS
                    	(SELECT TASK_ID
                    		FROM WFS.TASK_ACTION
                    		WHERE CREATED_USER_ID = :userId)
                    SELECT *
                    FROM WFS.TASK T
                    WHERE ID in
                    		(SELECT TASK_ID
                    			FROM TBL)
                                        """,
            countQuery = """
                    SELECT COUNT(1)
                    FROM
                    	(SELECT 1
                    		FROM WFS.TASK_ACTION
                    		WHERE CREATED_USER_ID = :userId
                    		GROUP BY TASK_ID) TBL
                    """)
    Page<Task> findAllByActionCreatedUserId(Pageable pageable, @Param(value = "userId") long userId);


    Page<Task> findAllByAssignedUserIdAndClosedFalse(Pageable pageable, long assignedUserId);

    Page<Task> findAllByCreatedUserIdAndClosedFalse(Pageable pageable, long createdUserId);
}
