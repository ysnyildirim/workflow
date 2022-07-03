package com.yil.workflow.repository;

import com.yil.workflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


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

}
