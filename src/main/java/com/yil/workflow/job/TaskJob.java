/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.job;

import com.yil.workflow.base.ApiConstant;
import com.yil.workflow.base.PageDto;
import com.yil.workflow.dto.*;
import com.yil.workflow.model.Properties;
import com.yil.workflow.repository.PropertiesDao;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@EnableScheduling
@EnableAsync
@RequiredArgsConstructor
public class TaskJob {
    public static final String upper = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static boolean stopped = true;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
    private final PropertiesDao propertiesDao;
    private FlowDto[] startupFlows = null;

  //  @Scheduled(fixedRate = 25, initialDelay = 3 * 1000)
    public void finish() {
        for (long i = 1; i <= 1000; i++) {
            if (isClosed())
                return;
            try {
                finishTask(i);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isClosed() {
        return stopped;
    }

    public void finishTask(long uId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> m3 = new HashMap<>();
        m3.put("userId", String.valueOf(uId));
        ResponseEntity<PageTaskResponce> pageDtoResponseEntity = restTemplate.getForEntity("http://localhost:8087/api/wf/v1/tasks/assigned={userId}", PageTaskResponce.class, m3);
        if (pageDtoResponseEntity.getStatusCode().isError()) {
            System.out.println("Cevap alınamadı");
            return;
        }
        PageTaskResponce myTasks = pageDtoResponseEntity.getBody();
        for (TaskDto task : myTasks.getContent()) {
            continueTask(uId, task.getId());
        }
    }

    public void continueTask(long uId, long taskId) {
        Map<String, String> map = new HashMap<>();
        map.put("taskId", String.valueOf(taskId));
        map.put("userId", String.valueOf(uId));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ActionDto[]> pageActionDtoResponseEntity = restTemplate.getForEntity("http://localhost:8087/api/wf/v1/tasks/{taskId}/actions/{userId}/next", ActionDto[].class, map);
        if (pageActionDtoResponseEntity.getStatusCode().isError()) {
            System.out.println("Task ilerletilemedi." + pageActionDtoResponseEntity.getStatusCode());
            return;
        }
        if (pageActionDtoResponseEntity.getBody().length == 0)
            return;
        ActionDto action = pageActionDtoResponseEntity.getBody()[(new Random().nextInt(0, pageActionDtoResponseEntity.getBody().length))];
        TaskActionRequest request = generateTaskAction(action);
        Map<String, String> map2 = new HashMap<>();
        map2.put("taskId", String.valueOf(taskId));
        HttpHeaders h2 = new HttpHeaders();
        h2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        h2.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(uId));
        HttpEntity<TaskActionRequest> he2 = new HttpEntity<>(request, h2);
        ResponseEntity<TaskActionDto> responseEntity = restTemplate.exchange("http://localhost:8087/api/wf/v1/tasks/{taskId}/actions", HttpMethod.POST, he2, TaskActionDto.class, map2);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("    " + simpleDateFormat.format(new Date()) + " Continue: userId:" + uId + ", taskId:" + responseEntity.getBody().getTaskId() + ", taskActionid:" + responseEntity.getBody().getTaskActionId());
            //continueTask(request.getAssignedUserId(), taskId);
        } else
            System.out.println("Task ilerletilemedi." + responseEntity.getStatusCode());
    }

    private TaskActionRequest generateTaskAction(ActionDto action) {
        TaskActionRequest taskActionRequest = new TaskActionRequest();
        taskActionRequest.setActionId(action.getId());
        taskActionRequest.setDocuments(generateDocumentArr());
        taskActionRequest.setMessages(generateMessages());
        taskActionRequest.setAssignedUserId(new Random().nextLong(2, 11));
        return taskActionRequest;
    }

    public TaskActionDocumentRequest[] generateDocumentArr() {
        List<TaskActionDocumentRequest> lst = new ArrayList<>();
        int k = new Random().nextInt(5);
        for (int i = 0; i < k; i++)
            lst.add(generateDocument());
        return lst.toArray(TaskActionDocumentRequest[]::new);
    }

    private TaskActionMessageRequest[] generateMessages() {
        List<TaskActionMessageRequest> lst = new ArrayList<>();
        int k = new Random().nextInt(5);
        for (int i = 0; i < k; i++)
            lst.add(generateMessage());
        return lst.toArray(TaskActionMessageRequest[]::new);
    }

    public TaskActionDocumentRequest generateDocument() {
        byte[] array = new byte[new Random().nextInt(100000000, 100000001)];
        new Random().nextBytes(array);
        return TaskActionDocumentRequest.builder()
                .name(randomString(15))
                .content(array)
                .extension(randomString(3))
                .build();
    }

    private TaskActionMessageRequest generateMessage() {
        int k = new Random().nextInt(50, 4000);
        int s = new Random().nextInt(5, 100);
        return TaskActionMessageRequest.builder()
                .content(randomString(k))
                .subject(randomString(s)).build();
    }

    private String randomString(int i) {
        StringBuilder s = new StringBuilder();
        for (int k = 0; k < i; k++)
            s.append(upper.toCharArray()[new Random().nextInt(upper.length())]);
        return s.toString();
    }

    @Scheduled(fixedDelay = 15 * 1000, initialDelay = 1 * 500)
    public void controlClosed() {
        Properties properties = propertiesDao.findById(1).orElse(null);
        stopped = properties.getValue().equals("0");
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1 * 1000)
    public void generate() {
        if (isClosed())
            return;
        if (startupFlows == null) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<FlowDto[]> responseEntity = restTemplate.getForEntity("http://localhost:8087/api/wf/v1/flows/start", FlowDto[].class);
            if (responseEntity.getStatusCode().isError()) {
                System.out.println("Başlangıç flow alınamadı." + responseEntity.getStatusCode());
                return;
            }
            startupFlows = responseEntity.getBody();
        }
        createTask(new Random().nextLong(2, 1001));
    }

    private void createTask(long uId) {
        if (startupFlows.length == 0) return;
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10; i++) {
            System.out.println(simpleDateFormat.format(new Date()) + "start");
            FlowDto flow = startupFlows[(new Random().nextInt(0, startupFlows.length))];
            HttpHeaders h2 = new HttpHeaders();
            h2.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            h2.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(uId));

            ResponseEntity<ActionDto[]> startActionsDto = restTemplate.exchange("http://localhost:8087/api/wf/v1/actions/starts/{flowId}", HttpMethod.GET, new HttpEntity<>(h2), ActionDto[].class, Map.of("flowId", flow.getId().toString()));
            if (startActionsDto.getStatusCode().isError()) {
                System.out.println("Başlangıç action alınamadı." + startActionsDto.getStatusCode());
                return;
            }
            if (startActionsDto.getBody().length == 0)
                return;
            System.out.println(simpleDateFormat.format(new Date()) + "getstartactions");
            ActionDto action = startActionsDto.getBody()[new Random().nextInt(0, startActionsDto.getBody().length)];
            TaskRequest request = generateTaskRequest(flow.getId(), action);
            HttpHeaders h3 = new HttpHeaders();
            h3.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            h3.add(ApiConstant.AUTHENTICATED_USER_ID, String.valueOf(uId));
            System.out.println(simpleDateFormat.format(new Date()) + "savetask");
            ResponseEntity<TaskResponse> taskResponseResponseEntity = restTemplate.exchange("http://localhost:8087/api/wf/v1/tasks", HttpMethod.POST, new HttpEntity<>(request, h3), TaskResponse.class);
            if (taskResponseResponseEntity.getStatusCode().is2xxSuccessful()) {
                System.out.println(simpleDateFormat.format(new Date()) + " Created-> userId:" + uId + ", taskId:" + taskResponseResponseEntity.getBody().getTaskId() + ", actionid:" + action.getId());
                //continueTask(request.getActionRequest().getAssignedUserId(), taskResponseResponseEntity.getBody().getTaskId());
            } else
                System.out.println("Task aksiyon oluşturulamadı." + taskResponseResponseEntity.getStatusCode());

        }
    }

    private TaskRequest generateTaskRequest(long flowId, ActionDto action) {
        TaskRequest request = new TaskRequest();
        request.setStartDate(new Date());
        request.setEstimatedFinishDate(DateUtils.addHours(request.getStartDate(), new Random().nextInt(10, 24 * 10)));
        request.setFinishDate(DateUtils.addHours(request.getStartDate(), new Random().nextInt(1, 24 * 10)));
        request.setPriorityTypeId(new Random().nextInt(1, 5));
        request.setActionRequest(generateTaskAction(action));
        return request;
    }

    public static class PageTaskResponce extends PageDto<TaskDto> {
        public PageTaskResponce() {
        }
    }
}
