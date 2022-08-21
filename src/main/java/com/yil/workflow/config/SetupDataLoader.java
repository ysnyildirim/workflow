package com.yil.workflow.config;

import com.yil.workflow.dto.*;
import com.yil.workflow.model.Properties;
import com.yil.workflow.model.*;
import com.yil.workflow.repository.*;
import com.yil.workflow.service.*;
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
    ActionPermissionDao actionPermissionDao;
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
    @Autowired
    private ActionPermissionTypeDao actionPermissionTypeDao;

    @Autowired
    private ActionTargetTypeDao actionTargetTypeDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        initStatus();
        initPriorityTypes();
        initStepType();
        initActionPermissionTypes();
        initActionTargetTypes();
        initProperties();

        try {
            //initSikayetFlow();

            //  for (int i = 0; i < 100; i++) generateFlow(new Random().nextLong(1, 50));
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
        PriorityTypeService.Dusuk = PriorityType.builder()
                .id(1)
                .name("DÜşük")
                .description("Çok az etkisi olan veya hiç etkisi olmayan önemsiz işlem.")
                .build();
        priorityTypeDao.save(PriorityTypeService.Dusuk);
        PriorityTypeService.Orta = PriorityType.builder()
                .id(2)
                .name("Orta")
                .description("Kolayca çözülebilecek küçük sorun.")
                .build();
        priorityTypeDao.save(PriorityTypeService.Orta);
        PriorityTypeService.Yuksek = PriorityType.builder()
                .id(3)
                .name("Yüksek")
                .description("İlerlemeyi etkileme potansiyeli var.")
                .build();
        priorityTypeDao.save(PriorityTypeService.Yuksek);
        PriorityTypeService.Kritik = PriorityType.builder()
                .id(4)
                .name("Kritik")
                .description("Kritik bir sorun.")
                .build();
        priorityTypeDao.save(PriorityTypeService.Kritik);
    }

    private void initStepType() {
        StepTypeService.Start = StepType.builder()
                .id(1)
                .name("Start")
                .description("Talebin başlatıldığı durumdur.Bu adımdaki aksiyonlar başlatma aksiyonu olarak kabul edilir.")
                .build();
        stepTypeDao.save(StepTypeService.Start);
        StepTypeService.Normal = StepType.builder()
                .id(2)
                .name("Normal")
                .description("Özel bir tanımı olmayan düzenli bir durum.")
                .build();
        stepTypeDao.save(StepTypeService.Normal);
        StepTypeService.Complete = StepType.builder()
                .id(3)
                .name("Complete")
                .description("Talebin normal şekilde tamamlandığını belirten durumdur")
                .build();
        stepTypeDao.save(StepTypeService.Complete);
    }

    private void initActionPermissionTypes() {
        ActionPermissionTypeService.Herkes = ActionPermissionType.builder()
                .id(1)
                .name("Herkes")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.Herkes);
        ActionPermissionTypeService.Atanan = ActionPermissionType.builder()
                .id(2)
                .name("Atanan kişi")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.Atanan);
        ActionPermissionTypeService.Olusturan = ActionPermissionType.builder()
                .id(3)
                .name("İşi oluşturan kişi")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.Olusturan);
        ActionPermissionTypeService.SonIslemYapan = ActionPermissionType.builder()
                .id(4)
                .name("Son işlem yapan kişi")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.SonIslemYapan);
        ActionPermissionTypeService.IslemYapanlar = ActionPermissionType.builder()
                .id(5)
                .name("İşlem yapmış kişiler")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.IslemYapanlar);
        ActionPermissionTypeService.YetkisiOlan = ActionPermissionType.builder()
                .id(6)
                .name("Yetkisi olan kişiler")
                .build();
        actionPermissionTypeDao.save(ActionPermissionTypeService.YetkisiOlan);
    }

    private void initActionTargetTypes() {
        ActionTargetTypeService.Ozel = ActionTargetType.builder()
                .id(1)
                .name("Özel biri")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.Ozel);
        ActionTargetTypeService.BelirliBiri = ActionTargetType.builder()
                .id(2)
                .name("Seçilen belirli biri")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.BelirliBiri);
        ActionTargetTypeService.Olusturan = ActionTargetType.builder()
                .id(3)
                .name("İşi oluşturan kişi")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.Olusturan);
        ActionTargetTypeService.SonIslemYapan = ActionTargetType.builder()
                .id(4)
                .name("Son işlem yapan kişi")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.SonIslemYapan);
        ActionTargetTypeService.IslemYapan = ActionTargetType.builder()
                .id(5)
                .name("İşlem yapan kişi")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.IslemYapan);
        ActionTargetTypeService.IslemYapanFarkliSonKisi = ActionTargetType.builder()
                .id(6)
                .name("İşlem yapan farklı son kişi")
                .build();
        actionTargetTypeDao.save(ActionTargetTypeService.IslemYapanFarkliSonKisi);
    }

    private void initProperties() {
        addProperties(Properties.builder().id(1).description("Auto task generator").value("0").build());
    }

    private void addStatus(Status status) {
        if (statusDao.existsById(status.getId()))
            return;
        statusDao.save(status);
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
            int stepTypeId = StepTypeService.Normal.getId();
            if (i == 0) stepTypeId = StepTypeService.Start.getId();
            else if (i == k - 1) stepTypeId = StepTypeService.Complete.getId();
            StepRequest stepRequest = new StepRequest();
            stepRequest.setName(randomString(20));
            stepRequest.setDescription(randomString(100));
            stepRequest.setEnabled(true);
            stepRequest.setCanAddDocument(new Random().nextBoolean());
            stepRequest.setCanAddMessage(new Random().nextBoolean());
            stepRequest.setStatusId(1);
            stepRequest.setStepTypeId(stepTypeId);
            StepResponse stepResponse = stepService.save(stepRequest, responce.getId(), userId);
        }
        List<Step> stepList = stepService.findAllByFlowIdAndDeletedTimeIsNull(responce.getId());
        stepList = stepList.stream().sorted(Comparator.comparingLong(Step::getId)).collect(Collectors.toList());
        for (int i = 0; i < stepList.size(); i++) {
            Step step = stepList.get(i);
            if (step.getStepTypeId().equals(StepTypeService.Complete.getId()))
                continue;
            int l = new Random().nextInt(5, 10);
            for (int j = 0; j < l; j++) {
                Long nextStepId = stepList.get(i + 1).getId();
                ActionRequest actionRequest = new ActionRequest();
                actionRequest.setName(randomString(20));
                actionRequest.setDescription(randomString(100));
                actionRequest.setEnabled(true);
                actionRequest.setNextStepId(nextStepId);
                actionRequest.setPermissionId(null);
                if (j == 0 || step.getStepTypeId().equals(StepTypeService.Start.getId())) {
                    actionRequest.setActionTargetTypeId(ActionTargetTypeService.BelirliBiri.getId());
                    actionRequest.setNextUserId(new Random().nextLong(2, 1001));
                } else {
                    if (new Random().nextBoolean()) {
                        actionRequest.setActionTargetTypeId(ActionTargetTypeService.Ozel.getId());
                    } else {
                        actionRequest.setActionTargetTypeId(new Random().nextInt(3, 7));
                    }
                }
                ActionResponse response = actionService.save(actionRequest, step.getId(), userId);

                List<Integer> targetTypes = new ArrayList<>();
                int targetTypeId;
                if (step.getStepTypeId().equals(StepTypeService.Start.getId())) {
                    // Herkes;
                    // YetkisiOlan;
                    targetTypes.add(new Random().nextBoolean() ? ActionPermissionTypeService.Herkes.getId() : ActionPermissionTypeService.YetkisiOlan.getId());
                } else {
                    // Atanan;
                    // Olusturan;
                    // SonIslemYapan;
                    // IslemYapanlar;
                    if (new Random().nextBoolean())
                        targetTypes.add(ActionPermissionTypeService.IslemYapanlar.getId());
                    else {
                        if (new Random().nextBoolean())
                            targetTypes.add(ActionPermissionTypeService.Olusturan.getId());
                        else
                            targetTypes.add(ActionPermissionTypeService.SonIslemYapan.getId());
                    }
                    if (new Random().nextBoolean() || j == 0)
                        targetTypes.add(ActionPermissionTypeService.Atanan.getId());
                }
                for (Integer item : targetTypes)
                    actionPermissionDao.save(ActionPermission.builder().id(ActionPermission.Pk.builder().actionId(response.getId()).actionPermissionTypeId(item).build()).build());
            }
        }
    }

    public static String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
    }

    private void initSikayetFlow() throws Exception {
        FlowRequest request = new FlowRequest();
        request.setName("Tüketici Şikayet");
        request.setDescription("Tüketici şikayet iş akışı");
        request.setEnabled(true);
        FlowResponse flowResponse = flowService.save(request, 1L);

        StepRequest s1 = new StepRequest();
        s1.setName("Şikayet oluştur");
        s1.setDescription("Şikayet oluşturma");
        s1.setEnabled(true);
        s1.setStatusId(1);
        s1.setStepTypeId(StepTypeService.Start.getId());
        StepResponse s1c = stepService.save(s1, flowResponse.getId(), 1l);

        StepRequest s2 = new StepRequest();
        s2.setName("İşletme Tarafından Cevaplama");
        s2.setDescription("Şikayetin işletme tarafından cevaplanması");
        s2.setEnabled(true);
        s2.setStatusId(1);
        s2.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s2c = stepService.save(s2, flowResponse.getId(), 1l);


        StepResponse s21c = stepService.save(StepRequest
                .builder()
                .name("İşletme Bilgi,Belge Talebi")
                .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal.getId())
                .build(), flowResponse.getId(), 1l);


        StepRequest s3 = new StepRequest();
        s3.setName("Tüketici 1. İtiraz");
        s3.setDescription("Tüketici tarafından işletme tarafından cevaplanan şikayete 1. itirazı");
        s3.setEnabled(true);
        s3.setStatusId(1);
        s3.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s3c = stepService.save(s3, flowResponse.getId(), 1l);

        StepRequest s4 = new StepRequest();
        s4.setName("İşletme Tarafından 1.İtirazın Cevaplanması");
        s4.setDescription("İşletme Tarafından 1.İtirazın Cevaplanması");
        s4.setEnabled(true);
        s4.setStatusId(1);
        s4.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s4c = stepService.save(s4, flowResponse.getId(), 1l);

        StepResponse s41c = stepService.save(StepRequest
                .builder()
                .name("İşletme Bilgi,Belge Talebi")
                .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal.getId())
                .build(), flowResponse.getId(), 1l);

        StepRequest s5 = new StepRequest();
        s5.setName("Tüketici 2. İtiraz");
        s5.setDescription("Tüketici tarafından işletme tarafından cevaplanan şikayete 2. itirazı");
        s5.setEnabled(true);
        s5.setStatusId(1);
        s5.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s5c = stepService.save(s5, flowResponse.getId(), 1l);

        StepRequest s6 = new StepRequest();
        s6.setName("İşletme Tarafından 2.İtirazın Cevaplanması");
        s6.setDescription("İşletme Tarafından 2.İtirazın Cevaplanması");
        s6.setEnabled(true);
        s6.setStatusId(1);
        s6.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s6c = stepService.save(s6, flowResponse.getId(), 1l);

        StepResponse s61c = stepService.save(StepRequest
                .builder()
                .name("İşletme Bilgi,Belge Talebi")
                .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                .enabled(true)
                .statusId(1)
                .stepTypeId(StepTypeService.Normal.getId())
                .build(), flowResponse.getId(), 1l);

        StepRequest s7 = new StepRequest();
        s7.setName("Tüketici 3. İtiraz");
        s7.setDescription("Tüketici tarafından işletme tarafından cevaplanan şikayete 3. itirazı");
        s7.setEnabled(true);
        s7.setStatusId(1);
        s7.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s7c = stepService.save(s7, flowResponse.getId(), 1l);

        StepRequest s8 = new StepRequest();
        s8.setName("Kurum incelemesi");
        s8.setDescription("Kurum tarafından şikayetin incelenmesi");
        s8.setEnabled(true);
        s8.setStatusId(1);
        s8.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s8c = stepService.save(s8, flowResponse.getId(), 1l);

        StepRequest s9 = new StepRequest();
        s9.setName("İşletmeden bilgi, belge talebi");
        s9.setDescription("Kurum tarafından işletmeden bilgi, belge talep edilmesi");
        s9.setEnabled(true);
        s9.setStatusId(1);
        s9.setStepTypeId(StepTypeService.Normal.getId());
        StepResponse s9c = stepService.save(s9, flowResponse.getId(), 1l);

        StepRequest s11 = new StepRequest();
        s11.setName("Şikayetin Kapatılması");
        s11.setDescription("Şikayetin kapatılması");
        s11.setEnabled(true);
        s11.setStatusId(1);
        s11.setStepTypeId(StepTypeService.Complete.getId());
        StepResponse s11c = stepService.save(s11, flowResponse.getId(), 1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Yeni")
                        .description("Yeni şikayet oluştur")
                        .enabled(true)
                        .nextStepId(s2c.getId())
                        .permissionId(2l)
                        .build(),
                s1c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("İşletme tarafından şikayeti cevaplandır")
                        .enabled(true)
                        .nextStepId(s3c.getId())
                        .permissionId(4l)
                        .build(),
                s2c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s21c.getId())
                        .permissionId(4l)
                        .build(),
                s2c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("Tüketici tarafından bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s2c.getId())
                        .permissionId(2l)
                        .build(),
                s21c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("İtiraz Et")
                        .description("Tüketici tarafından işletme cevabına 1.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s4c.getId())
                        .permissionId(2l)
                        .build(),
                s3c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Kapat")
                        .description("Tüketici tarafından şikayetin kapatılması")
                        .enabled(true)
                        .nextStepId(s11c.getId())
                        .permissionId(2l)
                        .build(),
                s3c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("İşletme tarafından şikayeti cevaplandır")
                        .enabled(true)
                        .nextStepId(s5c.getId())
                        .permissionId(4l)
                        .build(),
                s4c.getId(),
                1l);


        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s41c.getId())
                        .permissionId(4l)
                        .build(),
                s4c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("Tüketici tarafından bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s4c.getId())
                        .permissionId(2l)
                        .build(),
                s41c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("İtiraz Et")
                        .description("Tüketici tarafından işletme cevabına 2.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s6c.getId())
                        .permissionId(2l)
                        .build(),
                s5c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("İşletme tarafından şikayeti cevaplandır")
                        .enabled(true)
                        .nextStepId(s7c.getId())
                        .permissionId(4l)
                        .build(),
                s6c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talebi")
                        .description("İşletme tarafından tüketiciden bilgi, belge talebi")
                        .enabled(true)
                        .nextStepId(s61c.getId())
                        .permissionId(4l)
                        .build(),
                s6c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi Ekle")
                        .description("Tüketici tarafından bilgi eklenmesi")
                        .enabled(true)
                        .nextStepId(s6c.getId())
                        .permissionId(2l)
                        .build(),
                s61c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("İtiraz Et")
                        .description("Tüketici tarafından işletme cevabına 3.itiraz edilmesi")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(2l)
                        .build(),
                s7c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Cevapla")
                        .description("Şikayetin kurum tarafından cevaplanması")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Talep Et")
                        .description("Kurum tarafından işletmeden bilgi,belge talep edilmesi")
                        .enabled(true)
                        .nextStepId(s9c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Kapat")
                        .description("Şikayetin kurum tarafından kapatılması")
                        .enabled(true)
                        .nextStepId(s11c.getId())
                        .permissionId(5l)
                        .build(),
                s8c.getId(),
                1l);

        actionService.save(ActionRequest
                        .builder()
                        .name("Bilgi, Belge Gönder")
                        .description("İşletme tarafından kurumun bilgi,belge talebinin cevaplanması")
                        .enabled(true)
                        .nextStepId(s8c.getId())
                        .permissionId(4l)
                        .build(),
                s9c.getId(),
                1l);

    }

}
