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

            initSikayetFlow();

//            for (long i = 101; i <= 1226; i++) {
//                List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(i);
//                for (int j = 0; j < stepList.size(); j++) {
//                    Step step = stepList.get(j);
//                    if (step.getStepTypeId().equals(3))
//                        continue;
//                    else if (Arrays.asList(4, 5).contains(step.getStepTypeId())) //red veya iptal ise tamamland?? aksiyonu ekle
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
        addStepType(StepType.builder().id(1).name("Start").description("Talebin ba??lat??ld?????? durumdur.Bu ad??mdaki aksiyonlar ba??latma aksiyonu olarak kabul edilir.").build());
        addStepType(StepType.builder().id(2).name("Normal").description("??zel bir tan??m?? olmayan d??zenli bir durum.").build());
        addStepType(StepType.builder().id(3).name("Complete").description("Talebin normal ??ekilde tamamland??????n?? belirten durumdur").build());
        addStepType(StepType.builder().id(4).name("Denied").description("Talebin reddedildi??ini belirten durum.(i?? ba??lat??ld?? lakin reddedildi)").build());
        addStepType(StepType.builder().id(5).name("Cancelled").description("Talebin iptal edildi??ini belirten durum. (i?? ba??lad?? lakin tamamlanmad??.)").build());
    }

    private void initProperties() {
        addProperties(Properties.builder().id(1).description("Auto task generator").value("0").build());
    }

    private void initSikayetFlow() throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName("T??ketici ??ikayet");
        request.setDescription("T??ketici ??ikayet i?? ak??????");
        request.setEnabled(true);
        FlowResponse flowResponse = flowService.save(request, 1L);

        StepRequest s1 = new StepRequest();
        s1.setName("??ikayet olu??tur");
        s1.setDescription("??ikayet olu??turma");
        s1.setEnabled(true);
        s1.setStatusId(1);
        s1.setStepTypeId(StepTypeService.Start);
        StepResponse s1c = stepService.save(s1, flowResponse.getId(), 1l);

        StepRequest s2 = new StepRequest();
        s2.setName("????letme Taraf??ndan Cevaplama");
        s2.setDescription("??ikayetin i??letme taraf??ndan cevaplanmas??");
        s2.setEnabled(true);
        s2.setStatusId(1);
        s2.setStepTypeId(StepTypeService.Normal);
        StepResponse s2c = stepService.save(s2, flowResponse.getId(), 1l);


        StepResponse s21c = stepService.save(StepRequest
                .builder()
                .name("????letme Bilgi,Belge Talebi")
                .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal)
                .build(), flowResponse.getId(), 1l);


        StepRequest s3 = new StepRequest();
        s3.setName("T??ketici 1. ??tiraz");
        s3.setDescription("T??ketici taraf??ndan i??letme taraf??ndan cevaplanan ??ikayete 1. itiraz??");
        s3.setEnabled(true);
        s3.setStatusId(1);
        s3.setStepTypeId(StepTypeService.Normal);
        StepResponse s3c = stepService.save(s3, flowResponse.getId(), 1l);

        StepRequest s4 = new StepRequest();
        s4.setName("????letme Taraf??ndan 1.??tiraz??n Cevaplanmas??");
        s4.setDescription("????letme Taraf??ndan 1.??tiraz??n Cevaplanmas??");
        s4.setEnabled(true);
        s4.setStatusId(1);
        s4.setStepTypeId(StepTypeService.Normal);
        StepResponse s4c = stepService.save(s4, flowResponse.getId(), 1l);

        StepResponse s41c = stepService.save(StepRequest
                .builder()
                .name("????letme Bilgi,Belge Talebi")
                .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal)
                .build(), flowResponse.getId(), 1l);

        StepRequest s5 = new StepRequest();
        s5.setName("T??ketici 2. ??tiraz");
        s5.setDescription("T??ketici taraf??ndan i??letme taraf??ndan cevaplanan ??ikayete 2. itiraz??");
        s5.setEnabled(true);
        s5.setStatusId(1);
        s5.setStepTypeId(StepTypeService.Normal);
        StepResponse s5c = stepService.save(s5, flowResponse.getId(), 1l);

        StepRequest s6 = new StepRequest();
        s6.setName("????letme Taraf??ndan 2.??tiraz??n Cevaplanmas??");
        s6.setDescription("????letme Taraf??ndan 2.??tiraz??n Cevaplanmas??");
        s6.setEnabled(true);
        s6.setStatusId(1);
        s6.setStepTypeId(StepTypeService.Normal);
        StepResponse s6c = stepService.save(s6, flowResponse.getId(), 1l);

        StepResponse s61c = stepService.save(StepRequest
                .builder()
                .name("????letme Bilgi,Belge Talebi")
                .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal)
                .build(), flowResponse.getId(), 1l);

        StepRequest s7 = new StepRequest();
        s7.setName("T??ketici 3. ??tiraz");
        s7.setDescription("T??ketici taraf??ndan i??letme taraf??ndan cevaplanan ??ikayete 3. itiraz??");
        s7.setEnabled(true);
        s7.setStatusId(1);
        s7.setStepTypeId(StepTypeService.Normal);
        StepResponse s7c = stepService.save(s7, flowResponse.getId(), 1l);

        StepRequest s8 = new StepRequest();
        s8.setName("Kurum incelemesi");
        s8.setDescription("Kurum taraf??ndan ??ikayetin incelenmesi");
        s8.setEnabled(true);
        s8.setStatusId(1);
        s8.setStepTypeId(StepTypeService.Normal);
        StepResponse s8c = stepService.save(s8, flowResponse.getId(), 1l);

        StepRequest s9 = new StepRequest();
        s9.setName("????letmeden bilgi, belge talebi");
        s9.setDescription("Kurum taraf??ndan i??letmeden bilgi, belge talep edilmesi");
        s9.setEnabled(true);
        s9.setStatusId(1);
        s9.setStepTypeId(StepTypeService.Normal);
        StepResponse s9c = stepService.save(s9, flowResponse.getId(), 1l);

        StepRequest s11 = new StepRequest();
        s11.setName("??ikayetin Kapat??lmas??");
        s11.setDescription("??ikayetin kapat??lmas??");
        s11.setEnabled(true);
        s11.setStatusId(1);
        s11.setStepTypeId(StepTypeService.Complete);
        StepResponse s11c = stepService.save(s11, flowResponse.getId(), 1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Yeni")
                        .description("Yeni ??ikayet olu??tur")
                        .enabled(true)
                        .nextStepId(s2c.getId())
                        .permissionId(2l)
                        .build(),
                s1c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("????letme taraf??ndan ??ikayeti cevapland??r")
                        .enabled(true)
                        .nextStepId(s3c.getId())
                        .permissionId(4l)
                        .build(),
                s2c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s21c.getId())
                        .permissionId(4l)
                        .build(),
                s2c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("T??ketici taraf??ndan bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s2c.getId())
                        .permissionId(2l)
                        .build(),
                s21c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("??tiraz Et")
                        .description("T??ketici taraf??ndan i??letme cevab??na 1.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s4c.getId())
                        .permissionId(2l)
                        .build(),
                s3c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Kapat")
                        .description("T??ketici taraf??ndan ??ikayetin kapat??lmas??")
                        .enabled(true)
                        .nextStepId(s11c.getId())
                        .permissionId(2l)
                        .build(),
                s3c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("????letme taraf??ndan ??ikayeti cevapland??r")
                        .enabled(true)
                        .nextStepId(s5c.getId())
                        .permissionId(4l)
                        .build(),
                s4c.getId(),
                1l);


        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s41c.getId())
                        .permissionId(4l)
                        .build(),
                s4c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("T??ketici taraf??ndan bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s4c.getId())
                        .permissionId(2l)
                        .build(),
                s41c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("??tiraz Et")
                        .description("T??ketici taraf??ndan i??letme cevab??na 2.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s6c.getId())
                        .permissionId(2l)
                        .build(),
                s5c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("????letme taraf??ndan ??ikayeti cevapland??r")
                        .enabled(true)
                        .nextStepId(s7c.getId())
                        .permissionId(4l)
                        .build(),
                s6c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("????letme taraf??ndan t??keticiden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s61c.getId())
                        .permissionId(4l)
                        .build(),
                s6c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("T??ketici taraf??ndan bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s6c.getId())
                        .permissionId(2l)
                        .build(),
                s61c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("??tiraz Et")
                        .description("T??ketici taraf??ndan i??letme cevab??na 3.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(2l)
                        .build(),
                s7c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("??ikayetin kurum taraf??ndan cevaplanmas??")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talep Et")
                        .description("Kurum taraf??ndan i??letmeden bilgi,belge talep edilmesi")
                        .enabled(true)
                        .nextStepId(s9c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Kapat")
                        .description("??ikayetin kurum taraf??ndan kapat??lmas??")
                        .enabled(true)
                        .nextStepId(s11c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge G??nder")
                        .description("????letme taraf??ndan kurumun bilgi,belge talebinin cevaplanmas??")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(4l)
                        .build(),
                s9c.getId(),
                1l);

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
            else if (Arrays.asList(4, 5).contains(step.getStepTypeId())) //red veya iptal ise tamamland?? aksiyonu ekle
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
