package com.yil.workflow.service;

import com.yil.workflow.dto.StepDto;
import com.yil.workflow.dto.StepRequest;
import com.yil.workflow.dto.StepResponce;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.exception.StatusNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.exception.StepTypeNotFoundException;
import com.yil.workflow.model.Step;
import com.yil.workflow.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class StepService {

    private final StepRepository stepRepository;
    private final FlowService flowService;
    private final StepTypeService stepTypeService;
    private final StatusService statusService;

    public static StepDto toDto(Step step) throws NullPointerException {
        if (step == null)
            throw new NullPointerException("Step is null");
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

    public Step findByIdAndDeletedTimeIsNull(Long id) throws StepNotFoundException {
        return stepRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new StepNotFoundException());
    }

    public Step findByIdAndFlowIdAndDeletedTimeIsNull(Long id, Long flowId) throws StepNotFoundException {
        return stepRepository.findByIdAndFlowIdAndDeletedTimeIsNull(id, flowId).orElseThrow(() -> new StepNotFoundException());
    }

    public List<Step> findAllByFlowIdAndDeletedTimeIsNull(Long flowId) {
        return stepRepository.findAllByFlowIdAndDeletedTimeIsNull(flowId);
    }

    public StepResponce save(StepRequest request, Long flowId, Long userId) throws FlowNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        if (!flowService.existsById(flowId))
            throw new FlowNotFoundException();
        Step step = new Step();
        step.setFlowId(flowId);
        return getStepResponce(request, userId, step);
    }

    private StepResponce getStepResponce(StepRequest request, Long userId, Step step) throws StepTypeNotFoundException, StatusNotFoundException {
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
        return StepResponce.builder().id(step.getId()).build();
    }

    public StepResponce replace(StepRequest request, Long stepId, Long userId) throws StepNotFoundException, StepTypeNotFoundException, StatusNotFoundException {
        Step step = findByIdAndDeletedTimeIsNull(stepId);
        return getStepResponce(request, userId, step);
    }

    public void delete(Long stepId, Long userId) throws StepNotFoundException {
        Step step = findByIdAndDeletedTimeIsNull(stepId);
        step.setDeletedUserId(userId);
        step.setDeletedTime(new Date());
        stepRepository.save(step);
    }

    public boolean existsById(Long id) {
        return stepRepository.existsById(id);
    }
}
