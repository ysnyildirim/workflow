package com.yil.workflow.service;

import com.yil.workflow.dto.StepDto;
import com.yil.workflow.dto.StepRequest;
import com.yil.workflow.dto.StepResponse;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.Step;
import com.yil.workflow.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StepService {

    private final StepRepository stepRepository;
    private final FlowService flowService;
    private final StepTypeService stepTypeService;
    private final StatusService statusService;

    public static StepDto convert(Step step) {
        StepDto dto = new StepDto();
        dto.setId(step.getId());
        dto.setDescription(step.getDescription());
        dto.setEnabled(step.getEnabled());
        dto.setName(step.getName());
        dto.setFlowId(step.getFlowId());
        dto.setStepTypeId(step.getStepTypeId());
        dto.setStatusId(step.getStatusId());
        return dto;
    }


    @Transactional(readOnly = true)
    public Step findByIdAndStepTypeIdAndDeletedTimeIsNull(Long id, Integer stepTypeId) throws StepNotFoundException {
        return stepRepository.findByIdAndStepTypeIdAndDeletedTimeIsNull(id, stepTypeId).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Step> findByStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(int stepTypeId) {
        return stepRepository.findByStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(stepTypeId);
    }

    @Transactional(readOnly = true)
    public List<Step> findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(long flowId, int stepTypeId) {
        return stepRepository.findAllByFlowIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(flowId, stepTypeId);
    }

    @Transactional(readOnly = true)
    public boolean existsByIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(long id, int stepTypeId) {
        return stepRepository.existsByIdAndStepTypeIdAndEnabledTrueAndDeletedTimeIsNull(id, stepTypeId);
    }

    @Transactional(readOnly = true)
    public Step findByIdAndFlowIdAndDeletedTimeIsNull(Long id, Long flowId) throws StepNotFoundException {
        return stepRepository.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Step> findAllByFlowIdAndDeletedTimeIsNull(Long flowId) {
        return stepRepository.findAllByFlowIdAndDeletedTimeIsNull(flowId);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public StepResponse save(StepRequest request, Long flowId, Long userId) throws FlowNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        if (!flowService.existsById(flowId))
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
        step.setCreatedUserId(userId);
        step.setCreatedTime(new Date());
        step = stepRepository.save(step);
        return StepResponse.builder().id(step.getId()).build();
    }

    @Transactional(rollbackFor = {Throwable.class})
    public StepResponse replace(StepRequest request, Long stepId, Long userId) throws StepNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        Step step = findByIdAndDeletedTimeIsNull(stepId);
        return getStepResponce(request, userId, step);
    }

    @Transactional(readOnly = true)
    public Step findByIdAndDeletedTimeIsNull(Long id) throws StepNotFoundException {
        return stepRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(StepNotFoundException::new);
    }

    @Transactional(rollbackFor = {Throwable.class})
    public void delete(Long stepId, Long userId) throws StepNotFoundException {
        Step step = stepRepository.findByIdAndDeletedTimeIsNull(stepId).orElseThrow(StepNotFoundException::new);
        step.setDeletedUserId(userId);
        step.setDeletedTime(new Date());
        stepRepository.save(step);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return stepRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Step findByIdAndEnabledTrueAndDeletedTimeIsNull(long id) throws StepNotFoundException {
        return stepRepository.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(StepNotFoundException::new);
    }
}
