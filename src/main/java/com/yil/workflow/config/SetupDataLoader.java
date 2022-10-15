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
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ActionNotificationTargetTypeDao actionNotificationTargetTypeDao;
    @Autowired
    private ActionNotificationDao actionNotificationDao;
    @Autowired
    private ActionNotificationService actionNotificationService;
    @Autowired
    private ActionNotificationTargetService actionNotificationTargetService;
    @Autowired
    private ActionNotificationTargetDao actionNotificationTargetDao;

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
        initActionNotificationTargetType();
        initProperties();
        try {
            // for (int i = 0; i < 100; i++) generateFlow(new Random().nextLong(1, 50));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initStatus() {
        statusDao.save(Status.builder().id(1).name("Start").description("Started").build());
        statusDao.save(Status.builder().id(2).name("Continue").description("Continue").build());
        statusDao.save(Status.builder().id(3).name("Finished").description("Finished").build());
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

    private void initActionNotificationTargetType() {
        ActionNotificationTargetTypeService.BelirliBiri = ActionNotificationTargetType.builder()
                .id(1)
                .name("Seçilen belirli biri")
                .build();
        actionNotificationTargetTypeDao.save(ActionNotificationTargetTypeService.BelirliBiri);
        ActionNotificationTargetTypeService.IsiOlusturan = ActionNotificationTargetType.builder()
                .id(2)
                .name("İşi oluşturan kişi")
                .build();
        actionNotificationTargetTypeDao.save(ActionNotificationTargetTypeService.IsiOlusturan);
        ActionNotificationTargetTypeService.SonIslemYapan = ActionNotificationTargetType.builder()
                .id(3)
                .name("Son işlem yapan kişi")
                .build();
        actionNotificationTargetTypeDao.save(ActionNotificationTargetTypeService.SonIslemYapan);
        ActionNotificationTargetTypeService.IslemYapan = ActionNotificationTargetType.builder()
                .id(4)
                .name("İşlem yapan kişi")
                .build();
        actionNotificationTargetTypeDao.save(ActionNotificationTargetTypeService.IslemYapan);
        ActionNotificationTargetTypeService.IslemYapanFarkliSonKisi = ActionNotificationTargetType.builder()
                .id(5)
                .name("İşlem yapan farklı son kişi")
                .build();
        actionNotificationTargetTypeDao.save(ActionNotificationTargetTypeService.IslemYapanFarkliSonKisi);
    }

    private void initProperties() {
        propertiesDao.save(Properties.builder().id(1).description("Auto task generator").value("0").build());
    }

    @Transactional(rollbackFor = {Throwable.class})
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
                ActionResponse actionResponse = actionService.save(actionRequest, step.getId(), userId);
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
                for (Integer item : targetTypes) {
                    ActionPermission actionPermission = new ActionPermission();
                    actionPermission.setActionPermissionTypeId(item);
                    actionPermission.setActionId(actionResponse.getId());
                    if (item.equals(ActionPermissionTypeService.YetkisiOlan.getId()))
                        actionPermission.setPermissionId(1l);
                    actionPermissionDao.save(actionPermission);
                }
                for (int m = 0; m < new Random().nextInt(1, 6); m++) {
                    ActionNotification actionNotification = ActionNotification
                            .builder()
                            .actionId(actionResponse.getId())
                            .message(randomString(100))
                            .subject(randomString(20))
                            .build();
                    actionNotification = actionNotificationDao.save(actionNotification);
                    for (int n = 0; n < new Random().nextInt(1, 10); n++) {
                        ActionNotificationTarget actionNotificationTarget = ActionNotificationTarget
                                .builder()
                                .actionNotificationTargetTypeId(new Random().nextInt(1, 6))
                                .actionNotificationId(actionNotification.getId())
                                .build();
                        if (actionNotificationTarget.getActionNotificationTargetTypeId().equals(ActionNotificationTargetTypeService.BelirliBiri.getId()))
                            actionNotificationTarget.setUserId(0l);
                        actionNotificationTarget = actionNotificationTargetDao.save(actionNotificationTarget);
                    }
                }
            }
        }
    }

    public static String randomString(int i) {
        String s = "";
        for (int k = 0; k < i; k++)
            s += upper.toCharArray()[new Random().nextInt(upper.length())];
        return s;
    }

    private void flowKullaniciOlusturma() throws Exception {
        FlowRequest flowRequest = new FlowRequest();
        flowRequest.setName("Kullanıcı Oluşturma");
        flowRequest.setDescription("Kullanıcı oluşturma işlemi");
        flowRequest.setEnabled(true);
        FlowResponse flowResponse = flowService.save(flowRequest, 1l);

        StepResponse sBasla = stepService.save(
                StepRequest.builder()
                        .name("Başla")
                        .description("Kullanıcı oluşturma")
                        .stepTypeId(StepTypeService.Start.getId())
                        .statusId(0)
                        .enabled(true)
                        .canAddDocument(false)
                        .canAddMessage(false)
                        .build(),
                flowResponse.getId(),
                1l);

        StepResponse sOnayla = stepService.save(
                StepRequest.builder()
                        .name("Aktivasyon")
                        .description("Kullanıcı aktivasyonu")
                        .stepTypeId(StepTypeService.Normal.getId())
                        .statusId(0)
                        .enabled(true)
                        .canAddDocument(false)
                        .canAddMessage(false)
                        .build(),
                flowResponse.getId(),
                1l);

        StepResponse sBitir = stepService.save(
                StepRequest.builder()
                        .name("Bitir")
                        .description("Kullanıcı oluşturuldu")
                        .stepTypeId(StepTypeService.Complete.getId())
                        .statusId(0)
                        .enabled(true)
                        .canAddDocument(false)
                        .canAddMessage(false)
                        .build(),
                flowResponse.getId(),
                1l);

        ActionResponse aKaydol = actionService.save(
                ActionRequest.builder()
                        .name("Kayıt Ol")
                        .description("Kayıt olmak için tıklayınız")
                        .enabled(true)
                        .actionTargetTypeId(ActionTargetTypeService.IslemYapan.getId())
                        .nextStepId(sOnayla.getId())
                        .build(),
                sBasla.getId(),
                1l);

        ActionResponse aAktiflestir = actionService.save(
                ActionRequest.builder()
                        .name("Onayla")
                        .description("Aktivasyon için tıklayınız")
                        .enabled(true)
                        .actionTargetTypeId(ActionTargetTypeService.IslemYapan.getId())
                        .nextStepId(sBitir.getId())
                        .build(),
                sOnayla.getId(),
                1l);

        ActionResponse aYeniAktiflestirme = actionService.save(
                ActionRequest.builder()
                        .name("Aktivasyon Kodu Gönder")
                        .description("Aktivasyon kodu göndermek için tıklayınız")
                        .enabled(true)
                        .actionTargetTypeId(ActionTargetTypeService.IslemYapan.getId())
                        .nextStepId(sOnayla.getId())
                        .build(),
                sOnayla.getId(),
                1l);

        ActionNotificationResponse nOlusturma = actionNotificationService.save(
                ActionNotificationRequest.builder()
                        .subject("Aktivasyon")
                        .message("Kullanıcınız oluşturuldu. Aktivasyon işlemi için tıklayınız")
                        .build(),
                aKaydol.getId());

        ActionNotificationResponse nAktivasyon = actionNotificationService.save(
                ActionNotificationRequest.builder()
                        .subject("Aktivasyon")
                        .message("Aktivasyon işlemi için tıklayınız")
                        .build(),
                aYeniAktiflestirme.getId());

        actionNotificationTargetService.save(
                ActionNotificationTargetRequest.builder()
                        .actionNotificationTargetTypeId(ActionNotificationTargetTypeService.IsiOlusturan.getId())
                        .build(),
                nOlusturma.getId());

        actionNotificationTargetService.save(
                ActionNotificationTargetRequest.builder()
                        .actionNotificationTargetTypeId(ActionNotificationTargetTypeService.IsiOlusturan.getId())
                        .build(),
                nAktivasyon.getId());

    }

}
