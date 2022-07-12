package com.yil.workflow.service;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Action;
import com.yil.workflow.model.Flow;
import com.yil.workflow.model.Step;
import com.yil.workflow.repository.FlowDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlowService {

    private final FlowDao flowDao;
    private final StepService stepService;
    private final ActionService actionService;

    public static FlowDto convert(Flow flow) {
        FlowDto dto = new FlowDto();
        dto.setId(flow.getId());
        dto.setDescription(flow.getDescription());
        dto.setEnabled(flow.isEnabled());
        dto.setName(flow.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean existsById(long id) {
        return flowDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) {
        return flowDao.existsByIdAndEnabledTrueAndDeletedTimeIsNull(id);
    }

    @Transactional(readOnly = true)
    public List<StartUpFlowResponce> getStartUpFlows(long userId) {
        List<StartUpFlowResponce> startupFlows = new ArrayList<>();
        List<Flow> flows = flowDao.findAllByDeletedTimeIsNullAndEnabledTrue();
        for (Flow flow : flows) {
            List<ActionDto> availableActions = new ArrayList<>();
            List<Step> steps = stepService.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flow.getId(), 1);
            for (Step step : steps) {
                List<Action> actions = actionService.findAllByStepIdAndEnabledTrueAndDeletedTimeIsNull(step.getId());
                for (Action action : actions) {
                    availableActions.add(ActionService.convert(action));
                }
            }
            if (availableActions.size() > 0) {
                StartUpFlowResponce startUpFlowResponce = new StartUpFlowResponce();
                startUpFlowResponce.setName(flow.getName());
                startUpFlowResponce.setDescription(flow.getDescription());
                startUpFlowResponce.setId(flow.getId());
                startUpFlowResponce.setActions(availableActions.toArray(ActionDto[]::new));
                startupFlows.add(startUpFlowResponce);
            }
        }
        return startupFlows;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public FlowResponse save(FlowRequest request, Long userId) {
        Flow flow = new Flow();
        return getFlowResponce(request, userId, flow);
    }

    private FlowResponse getFlowResponce(FlowRequest request, Long userId, Flow flow) {
        flow.setName(request.getName());
        flow.setDescription(request.getDescription());
        flow.setEnabled(request.getEnabled());
        flow.setCreatedUserId(userId);
        flow.setCreatedTime(new Date());
        flow = flowDao.save(flow);
        return FlowResponse.builder().id(flow.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public FlowResponse replace(FlowRequest request, Long flowId, Long userId) throws FlowNotFoundException {
        Flow flow = flowDao.findByIdAndDeletedTimeIsNull(flowId).orElseThrow(FlowNotFoundException::new);
        return getFlowResponce(request, userId, flow);
    }

    @Transactional(readOnly = true)
    public Flow findByIdAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long id, Long userId) throws FlowNotFoundException {
        Flow flow = flowDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowNotFoundException::new);
        flow.setDeletedUserId(userId);
        flow.setDeletedTime(new Date());
        flowDao.save(flow);
    }

    @Transactional(readOnly = true)
    public List<Flow> findAllByDeletedTimeIsNull() {
        return flowDao.findAllByDeletedTimeIsNull();
    }

}
