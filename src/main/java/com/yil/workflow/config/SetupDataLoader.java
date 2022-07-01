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
    private FlowDao flowDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StepTypeDao stepTypeDao;
    @Autowired
    private PriorityTypeDao priorityTypeDao;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TargetTypeDao targetTypeDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initStatus();
        initTargetTypes();
        initPriorityTypes();
        initStepType();
        initGroupUserTypes();


//        try {
//            generateGroups(1L);
//        } catch (Exception e) {
//
//        }



//        for (Action action : actionDao.findAll())
//            generateActionSource(action.getId(), 1);
//
//        for (Action action : actionDao.findAll())
//            generateActionTarget(action.getId(), 1);

        generateTask();

//        try {
//            for (int i = 0; i < 100; i++)
//                generateFlow(new Random().nextLong(1, 50));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Autowired
    private GroupUserTypeDao groupUserTypeDao;

    private void initGroupUserTypes() {
        addGroupUserType(GroupUserType.builder().id(1).name("ADMIN").description("ADMINs").build());
        addGroupUserType(GroupUserType.builder().id(2).name("MANAGER").description("MANAgers").build());
        addGroupUserType(GroupUserType.builder().id(3).name("USER").description("USers").build());
    }

    private void addGroupUserType(GroupUserType type) {
        if (groupUserTypeDao.existsById(type.getId()))
            return;
        groupUserTypeDao.save(type);
    }

    @Autowired
    private ActionDao actionDao;

    @Autowired
    private GroupDao groupDao;

    public void generateTask() {
        try {
            List<Flow> flows = flowDao.findAllByDeletedTimeIsNull();
            for (int i = 0; i < 1; i++) {
                Flow flow = flows.get(i);
                Step step = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flow.getId(), 1).get(0);
                Action action = actionService.findAllByStepIdAndDeletedTimeIsNull(step.getId()).get(0);
                for (int j = 0; j < 10000; j++) {
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
//            threadPoolTaskExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void taskStart(Flow flow, Action action) throws ActionNotFoundException, NotAvailableActionException, StepNotFoundException, TaskActionNotFoundException, YouDoNotHavePermissionException, FlowNotFoundException, PriorityNotFoundException {
            int u = new Random().nextInt(1, 500000);
            TaskRequest request = generateTaskRequest(flow, action);
            TaskResponse taskResponse = taskService.save(request, u);
            finishTask(taskResponse.getTaskId(), (long) u);
            System.out.println(taskResponse.getTaskId());
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
        ActionResponse response = actionService.save(request, stepId, userId);
        generateActionSource(response.getId(), userId);
        generateActionTarget(response.getId(), userId);
        return response;
    }

    private TaskRequest generateTaskRequest(Flow flow, Action action) {
        TaskRequest request = new TaskRequest();
        request.setFlowId(flow.getId());
        request.setStartDate(new Date());
        request.setFinishDate(new Date());
        request.setEstimatedFinishDate(new Date());
        request.setPriorityTypeId(1);
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

    private void initPriorityTypes() {
        addPriority(PriorityType.builder().id(1).name("Highest").description("This problem will block progress").build());
        addPriority(PriorityType.builder().id(2).name("High").description("Serious problem that could block progress").build());
        addPriority(PriorityType.builder().id(3).name("Medium").description("Has the potential to effect progress").build());
        addPriority(PriorityType.builder().id(4).name("Low").description("Minor problem or easily worked around").build());
        addPriority(PriorityType.builder().id(5).name("Lowest").description("Trivial problem with little or no impact on progress").build());

    }

    private void addPriority(PriorityType priority) {
        if (priorityTypeDao.existsById(priority.getId()))
            return;
        priorityTypeDao.save(priority);
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

    private void initTargetTypes() {
        addTarget(TargetType.builder().id(1).name("Task Creator").description("Task creator").build());
        addTarget(TargetType.builder().id(2).name("Action User").description("Action execute user").build());
        addTarget(TargetType.builder().id(3).name("Group Members").description("Group Members").build());
    }

    private void addTarget(TargetType target) {
        if (targetTypeDao.existsById(target.getId()))
            return;
        targetTypeDao.save(target);
    }

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupUserService groupUserService;

    public void generateGroups(long userId) throws GroupUserNotFoundException {
        for (int j = 0; j < 100; j++) {
            GroupRequest groupRequest = GroupRequest.builder().name(randomString(20)).description(randomString(100)).build();
            GroupResponse groupResponse = groupService.save(groupRequest, userId);
            generateGroupUsers(groupResponse.getId(), userId);
        }
    }

    private void generateGroupUsers(Long groupId, long userId) throws GroupUserNotFoundException {
        for (int j = 0; j < 100; j++) {
            GroupUserRequest groupRequest = GroupUserRequest.builder().groupUserTypeId(new Random().nextInt(1, 4)).userId(new Random().nextLong(1, 500000)).build();
            GroupUserResponse response = groupUserService.save(groupRequest, groupId, userId);
        }
    }

    @Autowired
    private ActionTargetService actionTargetService;

    private void generateActionTarget(long actionId, long userId) {
        try {
            Action action = actionService.findByIdAndDeletedTimeIsNull(actionId);
            Step step = stepService.findByIdAndDeletedTimeIsNull(action.getStepId());
            Flow flow = flowService.findByIdAndDeletedTimeIsNull(step.getFlowId());

            List<Group> groups = groupDao.findAll();
            int k = new Random().nextInt(1, 3);
            for (int i = 0; i < k; i++) {
                Long groupId = null;
                int t = new Random().nextInt(1, 4);
                if (t == 3) //grup
                {
                    Group group = null;
                    if (!groups.isEmpty())
                        group = groups.get(new Random().nextInt(1, groups.size()));
                    if (group != null)
                        groupId = group.getId();
                }
                actionTargetService.save(ActionTargetRequest.builder().targetTypeId(t).groupId(groupId).build(), actionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Autowired
    private ActionSourceService actionSourceService;

    private void generateActionSource(long actionId, long userId) {
        try {
            Action action = actionService.findByIdAndDeletedTimeIsNull(actionId);
            Step step = stepService.findByIdAndDeletedTimeIsNull(action.getStepId());
            Flow flow = flowService.findByIdAndDeletedTimeIsNull(step.getFlowId());

            List<Group> groups = groupDao.findAll();
            if (groups.isEmpty())
                return;
            int k = new Random().nextInt(1, 5);
            for (int i = 0; i < k; i++) {
                int targetTypeId;
                Long groupId = null;
                if (step.getStepTypeId().equals(1)) //başlangıç adımı sadece gruplara gitsin
                {
                    targetTypeId = 3;
                    Group group = groups.get(new Random().nextInt(1, groups.size()));
                    groupId = group.getId();
                } else
                    targetTypeId = new Random().nextInt(1, 3);
                actionSourceService.save(ActionSourceRequest.builder().targetTypeId(targetTypeId).groupId(groupId).build(), actionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
