package com.yil.workflow.service;

import com.yil.workflow.model.Step;
import com.yil.workflow.repository.StepRepository;
import com.yil.workflow.dto.StepDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class StepService {

    private final StepRepository stepRepository;

    @Autowired
    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    public static StepDto toDto(Step step) throws NullPointerException {
        if (step == null)
            throw new NullPointerException("Step is null");
        StepDto dto = new StepDto();
        dto.setId(step.getId());
        dto.setDescription(step.getDescription());
        dto.setEnabled(step.getEnabled());
        dto.setName(step.getName());
        dto.setFlowId(step.getFlowId());
        return dto;
    }

    public Step findById(Long id) throws EntityNotFoundException {
        return stepRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Step save(Step step) {
        return stepRepository.save(step);
    }

    public Page<Step> findAllByDeletedTimeIsNull(Pageable pageable) {
        return stepRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
