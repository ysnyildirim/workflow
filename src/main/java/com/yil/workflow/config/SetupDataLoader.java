package com.yil.workflow.config;

import com.yil.workflow.model.ActionType;
import com.yil.workflow.model.Priority;
import com.yil.workflow.model.Status;
import com.yil.workflow.model.StepType;
import com.yil.workflow.repository.ActionTypeDao;
import com.yil.workflow.repository.PriorityRepository;
import com.yil.workflow.repository.StatusRepository;
import com.yil.workflow.repository.StepTypeDao;
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
        initStatus();
        initPriority();
        initActionType();
        initStepType();
    }

    private void initStepType() {
        addStepType(StepType.builder().id(1).name("Start").description("Should only be one per process. This state is the state into which a new Request is placed when it is created.").build());
        addStepType(StepType.builder().id(2).name("Normal").description("A regular state with no special designation.").build());
        addStepType(StepType.builder().id(3).name("Complete").description("A state signifying that any Request in this state have completed normally.").build());
        addStepType(StepType.builder().id(4).name("Denied").description("A state signifying that any Request in this state has been denied (e.g. never got started and will not be worked on).").build());
        addStepType(StepType.builder().id(5).name("Cancelled").description("A state signifying that any Request in this state has been cancelled (e.g. work was started but never completed).").build());
    }

    @Autowired
    private StepTypeDao stepTypeDao;

    private void addStepType(StepType stepType) {
        if (stepTypeDao.existsById(stepType.getId()))
            return;
        stepTypeDao.save(stepType);
    }

    private void initActionType() {
        addActionType(ActionType.builder().id(1).name("Approve").description("The actioner is suggesting that the request should move to the next state.").build());
        addActionType(ActionType.builder().id(2).name("Deny").description("The actioner is suggesting that the request should move to the previous state").build());
        addActionType(ActionType.builder().id(3).name("Cancel").description("The actioner is suggesting that the request should move to the Cancelled state in the process.").build());
        addActionType(ActionType.builder().id(4).name("Restart").description("The actioner suggesting that the request be moved back to the Start state in the process.").build());
        addActionType(ActionType.builder().id(5).name("Resolve").description("The actioner is suggesting that the request be moved all the way to the Completed state.").build());
    }

    @Autowired
    private ActionTypeDao actionTypeDao;

    private void addActionType(ActionType actionType) {
        if (actionTypeDao.existsById(actionType.getId()))
            return;
        actionTypeDao.save(actionType);
    }

    private void initPriority() {
        addPriority(Priority.builder().id(1).name("Highest").description("This problem will block progress").build());
        addPriority(Priority.builder().id(2).name("High").description("Serious problem that could block progress").build());
        addPriority(Priority.builder().id(3).name("Medium").description("Has the potential to effect progress").build());
        addPriority(Priority.builder().id(4).name("Low").description("Minor problem or easily worked around").build());
        addPriority(Priority.builder().id(5).name("Lowest").description("Trivial problem with little or no impact on progress").build());

    }

    private void addPriority(Priority priority) {
        if (priorityRepository.existsById(priority.getId()))
            return;
        priorityRepository.save(priority);
    }

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private StatusRepository statusRepository;

    private void initStatus() {
        addStatus(Status.builder().id(1).name("Start").description("Started").build());
        addStatus(Status.builder().id(2).name("Continue").description("Continue").build());
        addStatus(Status.builder().id(3).name("Finished").description("Finished").build());
    }

    private void addStatus(Status status) {
        if (statusRepository.existsById(status.getId()))
            return;
        statusRepository.save(status);
    }


}
