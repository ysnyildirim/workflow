package com.yil.workflow.config;

import com.yil.workflow.dto.*;
import com.yil.workflow.model.Properties;
import com.yil.workflow.model.*;
import com.yil.workflow.repository.PriorityTypeDao;
import com.yil.workflow.repository.PropertiesDao;
import com.yil.workflow.repository.StatusDao;
import com.yil.workflow.repository.StepTypeDao;
import com.yil.workflow.service.ActionService;
import com.yil.workflow.service.FlowService;
import com.yil.workflow.service.StepService;
import com.yil.workflow.service.StepTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@EnableAsync
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {

    public static final String upper = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @Autowired
    private ActionService actionService;
    @Autowired
    private StepService stepService;
    @Autowired
    private FlowService flowService;
    @Autowired
    private StepTypeDao stepTypeDao;
    @Autowired
    private PriorityTypeDao priorityTypeDao;
    @Autowired
    private StatusDao statusDao;
    @Autowired
    private PropertiesDao propertiesDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initStatus();
        initPriorityTypes();
        initStepType();
        initProperties();

        try {


//            for (long i = 101; i <= 1226; i++) {
//                List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(i);
//                for (int j = 0; j < stepList.size(); j++) {
//                    Step step = stepList.get(j);
//                    if (step.getStepTypeId().equals(3))
//                        continue;
//                    else if (Arrays.asList(4, 5).contains(step.getStepTypeId())) //red veya iptal ise tamamlandı aksiyonu ekle
//                    {
//
//                        Step completeStep = stepList.stream().filter(f -> f.getStepTypeId().equals(StepTypeService.Complete)).findFirst().orElse(null);
//                        if (completeStep == null)
//                            continue;
//                        ActionRequest actionRequest = generateActionRequest(completeStep.getId());
//                        ActionResponse response = actionService.save(actionRequest, stepList.get(j).getId(), 1L);
//                        continue;
//                    }
//                }
//            }

            //for (int i = 0; i < 100; i++) generateFlow(new Random().nextLong(1, 50));
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
        addStepType(StepType.builder().id(1).name("Start").description("Talebin başlatıldığı durumdur.Bu adımdaki aksiyonlar başlatma aksiyonu olarak kabul edilir.").build());
        addStepType(StepType.builder().id(2).name("Normal").description("Özel bir tanımı olmayan düzenli bir durum.").build());
        addStepType(StepType.builder().id(3).name("Complete").description("Talebin normal şekilde tamamlandığını belirten durumdur").build());
        addStepType(StepType.builder().id(4).name("Denied").description("Talebin reddedildiğini belirten durum.(iş başlatıldı lakin reddedildi)").build());
        addStepType(StepType.builder().id(5).name("Cancelled").description("Talebin iptal edildiğini belirten durum. (iş başladı lakin tamamlanmadı.)").build());
    }

    private void initProperties() {
        addProperties(Properties.builder().id(1).description("Auto task generator").value("0").build());
    }

    private void addStatus(Status status) {
        if (statusDao.existsById(status.getId()))
            return;
        statusDao.save(status);
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

    private void addProperties(Properties properties) {
        if (propertiesDao.existsById(properties.getId()))
            return;
        propertiesDao.save(properties);
    }

    public void generateFlow(long userId) throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        FlowResponse responce = flowService.save(request, userId);
        int k = new Random().nextInt(10, 15);
        for (int i = 0; i < k; i++) {
            int stepTypeId = 2;
            if (i == 0) stepTypeId = 1;
            else if (i == k - 1) stepTypeId = 3;
            else if (i == k - 2) stepTypeId = 4;
            else if (i == k - 3) stepTypeId = 5;
            generateStep(responce.getId(), userId, stepTypeId);
        }
        List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(responce.getId());
        stepList = stepList.stream().sorted(Comparator.comparingLong(Step::getId)).collect(Collectors.toList());
        for (int i = 0; i < stepList.size(); i++) {
            Step step = stepList.get(i);
            if (step.getStepTypeId().equals(3))
                continue;
            else if (Arrays.asList(4, 5).contains(step.getStepTypeId())) //red veya iptal ise tamamlandı aksiyonu ekle
            {
                Step completeStep = stepList.stream().filter(f -> f.getStepTypeId().equals(StepTypeService.Complete)).findFirst().orElse(null);
                if (completeStep == null)
                    continue;
                ActionRequest actionRequest = generateActionRequest(completeStep.getId());
                ActionResponse response = actionService.save(actionRequest, stepList.get(i).getId(), userId);
                continue;
            }
            int l = new Random().nextInt(10, 15);
            for (int j = 0; j < l; j++) {
                Long nextStepId = stepList.get(i + 1).getId();
                ActionRequest actionRequest = generateActionRequest(nextStepId);
                ActionResponse response = actionService.save(actionRequest, stepList.get(i).getId(), userId);
            }
        }
    }

    public static String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
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

    public ActionRequest generateActionRequest(Long nextStepId) {
        ActionRequest request = new ActionRequest();
        request.setName(randomString(20));
        request.setDescription(randomString(100));
        request.setEnabled(true);
        request.setNextStepId(nextStepId);
        request.setPermissionId(null);
        return request;
    }

}
