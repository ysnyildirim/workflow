package com.yil.workflow.config;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.*;
import com.yil.workflow.model.*;
import com.yil.workflow.repository.*;
import com.yil.workflow.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

       // generateTask();

//        try {
//            int k = new Random().nextInt(5,10);
//            for (int i = 0; i < k; i++)
//                generateFlow(1L);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    public void generateTask() {
        try {
            List<Flow> flows = flowRepository.findAllByDeletedTimeIsNull();


            for (Flow flow : flows) {
                Step step = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flow.getId(), 1).get(0);
                Action action = actionService.findAllByStepIdAndDeletedTimeIsNull(step.getId()).get(0);
                for (int i = 0; i < 100000; i++) {
                    int u = new Random().nextInt(1, 50);
                    TaskRequest request = generateTaskRequest(flow, action);
                    TaskResponce taskResponce = taskService.save(request, u);
                    finishTask(taskResponce.getTaskId(), (long) u);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishTask(Long taskId, Long userId) throws ActionNotFoundException, NotAvailableActionException, StepNotFoundException, YouDoNotHavePermissionException, TaskActionNotFoundException {
        TaskAction taskAction = taskActionService.getLastAction(taskId);
        Action currentAction = actionService.findById(taskAction.getActionId());
        Action action = actionService.findAllByStepIdAndDeletedTimeIsNull(currentAction.getNextStepId()).get(0);
        while (action != null) {
            TaskActionRequest request = generateTaskAction(action);
            TaskActionResponce taskActionResponce = taskActionService.save(request, taskAction.getTaskId(), userId);
            List<Action> actionList = actionService.findAllByStepIdAndDeletedTimeIsNull(action.getNextStepId());
            if (actionList.isEmpty())
                action = null;
            else
                action = actionList.get(0);
        }
    }

    @Autowired
    private TaskActionService taskActionService;


    @Transactional(timeoutString = "5000", rollbackFor = Exception.class)
    public void generateFlow(long userId) throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        FlowResponce responce = flowService.save(request, userId);
        int k = new Random().nextInt(5, 15);
        for (int i = 0; i < k; i++)
            generateStep(responce.getId(), userId);
    }

    @Transactional
    public void generateStep(long flowId, long userId) throws Exception {
        StepRequest request = new StepRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setStatusId(1);
        Step step = null;
        List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(flowId);
        if (stepList.size() == 0)
            request.setStepTypeId(1);
        else {
            request.setStepTypeId(2);
            step = stepList.get(stepList.size() - 1);
        }
        StepResponce responce = stepService.save(request, flowId, userId);
        if (step != null) {
            int k = new Random().nextInt(10, 15);
            for (int i = 0; i < k; i++)
                generateAction(step.getId(), userId, responce.getId());
        }
    }

    @Autowired
    private ActionService actionService;

    @Transactional
    public ActionResponce generateAction(long stepId, long userId, long nextStepId) throws Exception {
        ActionRequest request = new ActionRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setNextStepId(nextStepId);
        request.setActionTypeId(1);
        return actionService.save(request, stepId, userId);
    }

    @Autowired
    private StepService stepService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private FlowRepository flowRepository;

    private TaskRequest generateTaskRequest(Flow flow, Action action) {
        TaskRequest request = new TaskRequest();
        request.setFlowId(flow.getId());
        request.setStartDate(new Date());
        request.setFinishDate(new Date());
        request.setEstimatedFinishDate(new Date());
        request.setPriorityId(1);
        request.setActionRequest(generateTaskAction(action));
        return request;
    }

    private TaskActionRequest generateTaskAction(Action action) {
        TaskActionRequest taskActionRequest = new TaskActionRequest();
        taskActionRequest.setActionId(action.getId());
        taskActionRequest.setDocuments(generateDocumentArr());
        taskActionRequest.setMessages(generateMessages());
        return taskActionRequest;
    }

    private TaskActionMessageRequest[] generateMessages() {

        List<TaskActionMessageRequest> lst = new ArrayList<>();
        int k = new Random().nextInt(5);
        for (int i = 0; i < k; i++)
            lst.add(generateMessage());
        return lst.stream().toArray(TaskActionMessageRequest[]::new);
    }

    private TaskActionMessageRequest generateMessage() {
        int k = new Random().nextInt(50);
        int s = new Random().nextInt(5);
        return TaskActionMessageRequest.builder().content(randomString(k))
                .subject(randomString(s)).build();
    }

    public TaskActionDocumentRequest[] generateDocumentArr() {
        List<TaskActionDocumentRequest> lst = new ArrayList<>();
        int k = new Random().nextInt(5);
        for (int i = 0; i < k; i++)
            lst.add(generateDocument());
        return lst.stream().toArray(TaskActionDocumentRequest[]::new);
    }

    public TaskActionDocumentRequest generateDocument() {
        byte[] array = new byte[new Random().nextInt(1000)];
        new Random().nextBytes(array);
        Byte[] byteObject = ArrayUtils.toObject(array);
        return TaskActionDocumentRequest.builder()
                .uploadedDate(new Date())
                .name(randomString(15))
                .content(byteObject)
                .extension(randomString(3))
                .build();
    }

    public static final String upper = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
//        byte[] array = new byte[i];
//        new Random().nextBytes(array);
//        String generatedString = new String(array, Charset.forName("ASCII"));
//        return generatedString;
    }


    @Autowired
    private TaskService taskService;

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
