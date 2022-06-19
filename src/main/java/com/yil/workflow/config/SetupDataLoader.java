package com.yil.workflow.config;

import com.yil.workflow.model.TaskStatus;
import com.yil.workflow.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {


    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initTaskStatus();

    }

    @Autowired
    private TaskStatusService taskStatusService;

    private void initTaskStatus() {
        addTaskStatus("Open");
        addTaskStatus("Assigned");
        addTaskStatus("Resolved");
        addTaskStatus("Feed Back");
        addTaskStatus("Closed");
        addTaskStatus("Rejected");
    }

    private void addTaskStatus(String name) {
        if (taskStatusService.existsAllByNameAndDeletedTimeIsNull(name))
            return;
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(name);
        taskStatusService.save(taskStatus);
    }

}
