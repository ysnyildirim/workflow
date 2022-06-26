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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
@EnableAsync
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {


    public static final String upper = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private TaskActionService taskActionService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private StepService stepService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private FlowRepository flowRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StepTypeDao stepTypeDao;
    @Autowired
    private ActionTypeDao actionTypeDao;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TargetDao targetDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initStatus();
        initTarget();
        initPriority();
        initActionType();
        initStepType();

        // generateTask();

        //       try {
        //           for (int i = 0; i < 10; i++)
        //               generateFlow(1L);
        //       } catch (Exception e) {
        //           e.printStackTrace();
        //       }

    }

    public void generateTask() {
        try {
            List<Flow> flows = flowRepository.findAllByDeletedTimeIsNull();
            for (int i = 5; i < flows.size(); i++) {
                Flow flow = flows.get(i);
                Step step = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flow.getId(), 1).get(0);
                Action action = actionService.findAllByStepIdAndDeletedTimeIsNull(step.getId()).get(0);
                for (int j = 0; j < 100000; j++) {
                    //    threadPoolTaskExecutor.execute(() -> {
                    taskStart(flow, action);
                    //   System.out.println(Thread.currentThread().getId());
                    //  });
                }
            }
//            while (threadPoolTaskExecutor.getActiveCount() > 0) {
//                System.out.println("Number of tasks left: " + threadPoolTaskExecutor.getActiveCount());
//                Thread.sleep(5000);
//            }
            threadPoolTaskExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void taskStart(Flow flow, Action action) {
        try {
            int u = new Random().nextInt(1, 50);
            TaskRequest request = generateTaskRequest(flow, action);
            TaskResponse taskResponse = taskService.save(request, u);
            finishTask(taskResponse.getTaskId(), (long) u);
            System.out.println(taskResponse.getTaskId());
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
            TaskActionResponse taskActionResponse = taskActionService.save(request, taskAction.getTaskId(), userId);
            List<Action> actionList = actionService.findAllByStepIdAndDeletedTimeIsNull(action.getNextStepId());
            if (actionList.isEmpty())
                action = null;
            else
                action = actionList.get(0);
        }
    }

    @Transactional(timeoutString = "5000", rollbackFor = Exception.class)
    public void generateFlow(long userId) throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        FlowResponse responce = flowService.save(request, userId);
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
        StepResponse responce = stepService.save(request, flowId, userId);
        if (step != null) {
            int k = new Random().nextInt(10, 15);
            for (int i = 0; i < k; i++)
                generateAction(step.getId(), userId, responce.getId());
        }
    }

    @Transactional
    public ActionResponse generateAction(long stepId, long userId, long nextStepId) throws Exception {
        ActionRequest request = new ActionRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setNextStepId(nextStepId);
        request.setActionTypeId(1);
        return actionService.save(request, stepId, userId);
    }

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

    private void initStepType() {
        addStepType(StepType.builder().id(1).name("Start").description("Should only be one per process. This state is the state into which a new Request is placed when it is created.").build());
        addStepType(StepType.builder().id(2).name("Normal").description("A regular state with no special designation.").build());
        addStepType(StepType.builder().id(3).name("Complete").description("A state signifying that any Request in this state have completed normally.").build());
        addStepType(StepType.builder().id(4).name("Denied").description("A state signifying that any Request in this state has been denied (e.g. never got started and will not be worked on).").build());
        addStepType(StepType.builder().id(5).name("Cancelled").description("A state signifying that any Request in this state has been cancelled (e.g. work was started but never completed).").build());
    }

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

    private void initTarget() {
        addTarget(Target.builder().id(1).name("Creator").description("Task creator").build());
        addTarget(Target.builder().id(2).name("Stakeholders").description("Stakeholders").build());
        addTarget(Target.builder().id(3).name("Group Members").description("Group Members").build());
        addTarget(Target.builder().id(4).name("Flow Admins").description("Flow Admins").build());
    }

    private void addTarget(Target target) {
        if (targetDao.existsById(target.getId()))
            return;
        targetDao.save(target);
    }


}
