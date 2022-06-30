package com.yil.workflow.repository;

import com.yil.workflow.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TaskDao extends JpaRepository<Task, Long> {

    @Query(nativeQuery = true,
            value = "select * from task t\n" +
                    "where exists \n" +
                    "( \n" +
                    "select 1 from task_action ta \n" +
                    "where t.ID=ta.TASK_ID\n" +
                    "and ta.ID= (select max(ta2.ID) from TASK_ACTION ta2 where ta2.TASK_ID=ta.TASK_ID)\n" +
                    "and exists  (select 1 from ACTION_USER au where ta.ACTION_ID=au.ACTION_ID and au.USER_ID=1)\n" +
                    ");")
    Page<Task> getAllByUserId(Pageable pageable);


}
