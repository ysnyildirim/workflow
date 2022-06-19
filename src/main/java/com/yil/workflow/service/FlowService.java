package com.yil.workflow.service;

import com.yil.workflow.dto.FlowDto;
import com.yil.workflow.exception.FlowNotFoundException;
import com.yil.workflow.model.Flow;
import com.yil.workflow.repository.FlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class FlowService {

    private final FlowRepository flowRepository;

    @Autowired
    public FlowService(FlowRepository flowRepository) {
        this.flowRepository = flowRepository;
    }

    public static FlowDto toDto(Flow flow) throws NullPointerException {
        if (flow == null)
            throw new NullPointerException("Flow is null");
        FlowDto dto = new FlowDto();
        dto.setId(flow.getId());
        dto.setDescription(flow.getDescription());
        dto.setEnabled(flow.getEnabled());
        dto.setName(flow.getName());
        dto.setStartUpPermissionId(flow.getStartUpPermissionId());
        dto.setStartUpStepId(flow.getStartUpStepId());
        return dto;
    }

    public Flow findById(Long id) throws EntityNotFoundException {
        return flowRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Flow findByIdAndDeletedTimeIsNull(Long id) throws FlowNotFoundException {
        return flowRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new FlowNotFoundException());
    }

    public Flow save(Flow flow) {
        return flowRepository.save(flow);
    }

    public Page<Flow> findAllByDeletedTimeIsNull(Pageable pageable) {
        return flowRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
