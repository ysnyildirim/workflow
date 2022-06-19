package com.yil.workflow.config;

import com.yil.workflow.model.Priority;
import com.yil.workflow.model.Status;
import com.yil.workflow.service.PriorityService;
import com.yil.workflow.service.StatusService;
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
        initTaskPriority();
    }

    private void initTaskPriority() {
        addTaskPriority("Highest", "This problem will block progress");
        addTaskPriority("High", "Serious problem that could block progress");
        addTaskPriority("Medium", "Has the potential to effect progress");
        addTaskPriority("Low", "Minor problem or easily worked around");
        addTaskPriority("Lowest", "Trivial problem with little or no impact on progress");
    }

    private void addTaskPriority(String name, String description) {
        if (priorityService.existsAllByNameAndDeletedTimeIsNull(name))
            return;
        Priority entity = new Priority();
        entity.setName(name);
        entity.setDescription(description);
        priorityService.save(entity);
    }

    @Autowired
    private PriorityService priorityService;

    @Autowired
    private StatusService statusService;

    private void initTaskStatus() {
        addTaskStatus("Open", false);
        addTaskStatus("Assigned", false);
        addTaskStatus("Resolved", false);
        addTaskStatus("Feed Back", false);
        addTaskStatus("Closed", true);
        addTaskStatus("Rejected", true);
    }

    private void addTaskStatus(String name, Boolean isClosed) {
        if (statusService.existsAllByNameAndDeletedTimeIsNull(name))
            return;
        Status status = new Status();
        status.setName(name);
        status.setIsClosed(isClosed);
        statusService.save(status);
    }

}
