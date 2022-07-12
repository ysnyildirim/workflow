package com.yil.workflow.config;

import com.yil.workflow.dto.*;
import com.yil.workflow.model.*;
import com.yil.workflow.repository.FlowDao;
import com.yil.workflow.repository.PriorityTypeDao;
import com.yil.workflow.repository.StatusRepository;
import com.yil.workflow.repository.StepTypeDao;
import com.yil.workflow.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initStatus();
        initPriorityTypes();
        initStepType();

        try {
            // for (int i = 0; i < 100; i++) generateFlow(new Random().nextLong(1, 50));
            generateTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initStatus() {
        addStatus(Status.builder().id(1).name("Start").description("Started").build());
        addStatus(Status.builder().id(2).name("Continue").description("Continue").build());
        addStatus(Status.builder().id(3).name("Finished").description("Finished").build());
    }


    private void initPriorityTypes() {
        addPriority(PriorityType.builder().id(1).name("Highest").description("This problem will block progress").build());
        addPriority(PriorityType.builder().id(2).name("High").description("Serious problem that could block progress").build());
        addPriority(PriorityType.builder().id(3).name("Medium").description("Has the potential to effect progress").build());
        addPriority(PriorityType.builder().id(4).name("Low").description("Minor problem or easily worked around").build());
        addPriority(PriorityType.builder().id(5).name("Lowest").description("Trivial problem with little or no impact on progress").build());

    }

    private void initStepType() {
        addStepType(StepType.builder().id(1).name("Start").description("Should only be one per process. This state is the state into which a new Request is placed when it is created.").build());
        addStepType(StepType.builder().id(2).name("Normal").description("A regular state with no special designation.").build());
        addStepType(StepType.builder().id(3).name("Complete").description("A state signifying that any Request in this state have completed normally.").build());
        addStepType(StepType.builder().id(4).name("Denied").description("A state signifying that any Request in this state has been denied (e.g. never got started and will not be worked on).").build());
        addStepType(StepType.builder().id(5).name("Cancelled").description("A state signifying that any Request in this state has been cancelled (e.g. work was started but never completed).").build());
    }

    public void generateFlow(long userId) throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        FlowResponse responce = flowService.save(request, userId);
        int k = new Random().nextInt(5, 15);
        for (int i = 0; i < k; i++) {
            int stepTypeId = 2;
            if (i == 0) stepTypeId = 1;
            else if (i == k - 1) stepTypeId = 3;
            else if (i % 4 == 0) stepTypeId = 4;
            else if (i % 5 == 0) stepTypeId = 5;
            generateStep(responce.getId(), userId, stepTypeId);
        }
        List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(responce.getId());
        for (int i = 0; i < stepList.size() - 1; i++) {
            Step step = stepList.get(i);
            if (Arrays.asList(3, 4, 5).contains(step.getStepTypeId()))
                continue;
            int l = new Random().nextInt(10, 15);
            for (int j = 0; j < l; j++) {
                Long nextStepId = stepList.get(i + 1).getId();
                generateAction(stepList.get(i).getId(), userId, nextStepId, (i == 0));
            }
        }
    }

    private void addStatus(Status status) {
        if (statusRepository.existsById(status.getId()))
            return;
        statusRepository.save(status);
    }

    private void addPriority(PriorityType priority) {
        if (priorityTypeDao.existsById(priority.getId()))
            return;
        priorityTypeDao.save(priority);
    }

    private void addStepType(StepType stepType) {
        if (stepTypeDao.existsById(stepType.getId()))
            return;
        stepTypeDao.save(stepType);
    }

    public void generateStep(long flowId, long userId, int stepTypeId) throws Exception {
        StepRequest request = new StepRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setStatusId(1);
        request.setStepTypeId(stepTypeId);
        StepResponse responce = stepService.save(request, flowId, userId);

    }

    public ActionResponse generateAction(long stepId, long userId, Long nextStepId, boolean firstStep) throws Exception {
        ActionRequest request = new ActionRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setNextStepId(nextStepId);
        request.setPermissionId(null);
        ActionResponse response = actionService.save(request, stepId, userId);
        return response;
    }

    private String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
    }

    public void generateTask() {
        try {
            threadPoolTaskExecutor.execute(() -> {
                while (true) {
                    for (long uId = 1; uId < 1001l; uId++) {
                        try {
                            Pageable pageable = PageRequest.of(0, 1000);
                            Page<Task> myTasks = taskService.getMyTask(pageable, uId);
                            if (myTasks.isEmpty()) {
                                List<StartUpFlowResponce> flows = flowService.getStartUpFlows(uId);
                                StartUpFlowResponce flow = flows.get(new Random().nextInt(0, flows.size()));
                                for (int i = 0; i < 100; i++) {
                                    ActionDto action = flow.getActions()[new Random().nextInt(0, flow.getActions().length)];
                                    TaskRequest request = generateTaskRequest(flow.getId(), action);
                                    TaskResponse taskResponse = taskService.save(request, uId);
                                    System.out.println("Task aksiyon oluşturuldu: userId:" + uId + ", taskId:" + taskResponse.getTaskId() + ", actionid:" + action.getId());
                                }
                            } else {
                                for (Task task : myTasks) {
                                    List<ActionDto> actions = taskActionService.getNextActions(task.getId(), uId);
                                    if (!actions.isEmpty()) {
                                        ActionDto action = actions.get(new Random().nextInt(0, actions.size()));
                                        TaskActionRequest request = generateTaskAction(action);
                                        TaskAction taskAction = taskActionService.save(request, task.getId(), uId);
                                        System.out.println("Task ilerletildi: userId:" + uId + ", taskId:" + taskAction.getTaskId() + ", actionid:" + taskAction.getActionId());
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

           /* for (long userId = 1; userId < 1001l; userId++) {
                long uId = userId;
                threadPoolTaskExecutor.execute(() -> {
                    try {
                        while (true) {
                            List<Task> myTasks = taskService.getMyTasks(uId);
                            if (myTasks.isEmpty()) {
                                List<Flow> flows = flowService.getStartUpFlows(uId);
                                Flow flow = flows.get(new Random().nextInt(0, flows.size()));
                                List<Action> actions = actionService.getStartUpActions(flow.getId(), uId);
                                Action action = actions.get(new Random().nextInt(0, actions.size()));
                                TaskRequest request = generateTaskRequest(flow, action);
                                TaskResponse taskResponse = taskService.save(request, uId);
                                System.out.println("Task aksiyon oluşturuldu: userId:" + uId + ", taskId:" + taskResponse.getTaskId() + ", actionid:" + action.getId());
                            } else {
                                for (Task task : myTasks) {
                                    List<Action> actions = taskActionService.getNextActions(task.getId(), uId);
                                    if (!actions.isEmpty()) {
                                        Action action = actions.get(new Random().nextInt(0, actions.size()));
                                        TaskActionRequest request = generateTaskAction(action);
                                        TaskAction taskAction = taskActionService.save(request, task.getId(), uId);
                                        System.out.println("Task ilerletildi: userId:" + uId + ", taskId:" + taskAction.getTaskId() + ", actionid:" + taskAction.getActionId());
                                    }

                                }
                            }
                            Thread.sleep(10000);
                        }
                    } catch (Exception e) {
                        System.out.println(Thread.currentThread().getId());
                        e.printStackTrace();
                    }
                });
            }
*/
//            List<Flow> flows = flowDao.findAllByDeletedTimeIsNull();
//            for (int i = 0; i < 1; i++) {
//                Flow flow = flows.get(i);
//                Step step = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flow.getId(), 1).get(0);
//                Action action = actionService.findAllByStepIdAndDeletedTimeIsNull(step.getId()).get(0);
//                for (int j = 0; j < 10000; j++) {
//                    //    threadPoolTaskExecutor.execute(() -> {
//                    taskStart(flow, action);
//                    //   System.out.println(Thread.currentThread().getId());
//                    //  });
//                }
//            }
//            while (threadPoolTaskExecutor.getActiveCount() > 0) {
//                System.out.println("Number of tasks left: " + threadPoolTaskExecutor.getActiveCount());
//                Thread.sleep(5000);
//            }
//            threadPoolTaskExecutor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TaskRequest generateTaskRequest(long flowId, ActionDto action) {
        TaskRequest request = new TaskRequest();
        request.setStartDate(new Date());
        request.setFinishDate(new Date());
        request.setEstimatedFinishDate(new Date());
        request.setPriorityTypeId(1);
        request.setActionRequest(generateTaskAction(action));
        return request;
    }

    private TaskActionRequest generateTaskAction(ActionDto action) {
        TaskActionRequest taskActionRequest = new TaskActionRequest();
        taskActionRequest.setActionId(action.getId());
        taskActionRequest.setDocuments(generateDocumentArr());
        taskActionRequest.setMessages(generateMessages());
        taskActionRequest.setAssignedUserId(new Random().nextLong(1, 1001));
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


}
