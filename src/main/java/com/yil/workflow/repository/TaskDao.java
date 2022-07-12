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
                        update WFS.TASK t set IS_CLOSED=1
                        where t.ID=:taskId
                    """)
    void closedTask(@Param(value = "taskId") long taskId);

    @Query(nativeQuery = true,
            value = """
                    select * from WFS.TASK t
                    where t.IS_CLOSED=0 
                    and exists( 
                            select 1 from WFS.TASK_ACTION ta
                            where ta.TASK_ID=t.ID 
                            and ta.ASSIGNED_USER_ID=:userId
                            and ta.Id=(
                                select max(ta2.ID) from WFS.TASK_ACTION ta2
                                where ta2.TASK_ID=ta.TASK_ID))
                    """,
            countQuery = """
                    select count(t.ID) from WFS.TASK t
                    where t.IS_CLOSED=0
                    and exists(
                            select 1 from WFS.TASK_ACTION ta
                            where ta.TASK_ID=t.ID
                            and ta.ASSIGNED_USER_ID=:userId
                            and ta.Id=(
                                select max(ta2.ID) from WFS.TASK_ACTION ta2
                                where ta2.TASK_ID=ta.TASK_ID))
                    """)
    Page<Task> getMyTask(Pageable pageable, @Param(value = "userId") long userId);

}
