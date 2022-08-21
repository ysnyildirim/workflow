package com.yil.workflow.service;

import com.yil.workflow.dto.StepDto;
import com.yil.workflow.dto.StepRequest;
import com.yil.workflow.dto.StepResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.Step;
import com.yil.workflow.repository.FlowDao;
import com.yil.workflow.repository.StepDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StepService {

    private final StepDao stepDao;
    private final FlowDao flowDao;
    private final StepTypeService stepTypeService;
    private final StatusService statusService;

    public static StepDto convert(Step step) {
        StepDto dto = new StepDto();
        dto.setId(step.getId());
        dto.setDescription(step.getDescription());
        dto.setEnabled(step.isEnabled());
        dto.setName(step.getName());
        dto.setFlowId(step.getFlowId());
        dto.setStepTypeId(step.getStepTypeId());
        dto.setStatusId(step.getStatusId());
        return dto;
    }


    @Transactional(readOnly = true)
    public List<Step> findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(long flowId, int stepTypeId) {
        return stepDao.findAllByFlowIdAndStepTypeIdAndEnabledTrue(flowId, stepTypeId);
    }

    @Transactional(readOnly = true)
    public Step findByIdAndFlowIdAndDeletedTimeIsNull(Long id, Long flowId) throws StepNotFoundException {
        return stepDao.findByIdAndFlowId(id, flowId).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Step> findAllByFlowIdAndDeletedTimeIsNull(Long flowId) {
        return stepDao.findAllByFlowId(flowId);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public StepResponse save(StepRequest request, Long flowId, Long userId) throws FlowNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        if (!flowDao.existsById(flowId))
            throw new FlowNotFoundException();
        Step step = new Step();
        step.setFlowId(flowId);
        return getStepResponce(request, userId, step);
    }

    private StepResponse getStepResponce(StepRequest request, Long userId, Step step) throws StepTypeNotFoundException, StatusNotFoundException {
        if (!stepTypeService.existsById(request.getStepTypeId()))
            throw new StepTypeNotFoundException();
        if (!statusService.existsById(request.getStatusId()))
            throw new StatusNotFoundException();
        step.setName(request.getName());
        step.setDescription(request.getDescription());
        step.setEnabled(request.getEnabled());
        step.setStepTypeId(request.getStepTypeId());
        step.setStatusId(request.getStatusId());
        step.setCanAddDocument(request.getCanAddDocument());
        step.setCanAddMessage(request.getCanAddMessage());
        step.setCreatedUserId(userId);
        step.setCreatedTime(new Date());
        step = stepDao.save(step);
        return StepResponse.builder().id(step.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public StepResponse replace(StepRequest request, Long stepId, Long userId) throws StepNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        Step step = findById(stepId);
        return getStepResponce(request, userId, step);
    }

    public Step findById(Long stepId) throws StepNotFoundException {
        return stepDao.findById(stepId).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long stepId, Long userId) {
        stepDao.deleteById(stepId);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return stepDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public Step findByIdAndEnabledTrue(long id) throws StepNotFoundException {
        return stepDao.findByIdAndEnabledTrue(id).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Boolean existsByIdAndStepTypeId(long id, int stepTypeId) {
        return stepDao.existsByIdAndStepTypeId(id, stepTypeId);
    }

}
