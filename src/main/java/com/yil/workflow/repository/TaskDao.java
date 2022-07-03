package com.yil.workflow.repository;

import com.yil.workflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface TaskDao extends JpaRepository<Task, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = """
                        update WFS.TASK t set IS_CLOSED=1
                        where t.IS_CLOSED<>1
                        and exists(
                            select 1 from WFS.TASK_ACTION ta
                            where ta.Id=(
                                select max(ta2.ID) from WFS.TASK_ACTION ta2
                                where ta2.TASK_ID=ta.TASK_ID)
                            and exists(
                                select 1 from WFS.ACTION a
                                where ta.ACTION_ID=a.ID
                                and exists(
                                    select 1 from WFS.STEP s
                                    where s.ID=a.STEP_ID
                                    and s.STEP_TYPE_ID IN(3,4,5))
                                ))
                    """)
    void closedTask();

    @Query(nativeQuery = true,
            value = """
                    with tblAction as (
                        select *
                        from WFS.ACTION a
                        where a.ENABLED = 1
                          and a.DELETED_TIME IS NULL
                          and exists(select 1
                                     from WFS.STEP s
                                     where s.ID = a.STEP_ID
                                       and s.ENABLED = 1
                                       and s.DELETED_TIME IS NULL
                                       and s.STEP_TYPE_ID = 2
                            )
                          and (
                                (
                                        a.TARGET_TYPE_ID = 4
                                        and a.USER_ID = :userId)
                                or
                                (
                                        a.TARGET_TYPE_ID = 3
                                        and exists
                                            (
                                                select 1
                                                from WFS.GROUP_USER gu
                                                where gu.GROUP_ID = a.GROUP_ID
                                                  and gu.GROUP_USER_TYPE_ID = 3
                                                  and gu.USER_ID = :userId
                                            )
                                    )
                                or
                                a.TARGET_TYPE_ID in (1, 2)
                            )
                    )
                    select *
                    from WFS.TASK t
                    where t.IS_CLOSED = 0
                      and (exists
                               (select 1
                                from WFS.TASK_ACTION ta
                                where ta.TASK_ID = t.ID
                                  and ta.ID = (select max(ta2.ID) from WFS.TASK_ACTION ta2 where ta2.TASK_ID = t.ID)
                                  and exists(select 1
                                             from tblAction a
                                             where a.ID = ta.ACTION_ID
                                               and ((a.TARGET_TYPE_ID = 2 and ta.CREATED_USER_ID = :userId) or a.TARGET_TYPE_ID in (3, 4))))
                        or exists
                               (select 1
                                from WFS.TASK_ACTION ta
                                where ta.TASK_ID = t.ID
                                  and ta.CREATED_USER_ID = :userId
                                  and ta.ID = (select min(ta2.ID) from WFS.TASK_ACTION ta2 where ta2.TASK_ID = t.ID)
                                  and exists(select 1 from tblAction a where a.ID = ta.ACTION_ID and a.TARGET_TYPE_ID = 1))
                        )
                                        """)
    List<Task> getMyTasks(@Param(value = "userId") long userId);

}
