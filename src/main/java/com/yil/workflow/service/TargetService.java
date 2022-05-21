package com.yil.workflow.service;

import com.yil.workflow.dto.TargetDto;
import com.yil.workflow.model.Target;
import com.yil.workflow.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class TargetService {

    private final TargetRepository targetRepository;

    @Autowired
    public TargetService(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public static TargetDto toDto(Target target) throws NullPointerException {
        if (target == null)
            throw new NullPointerException("Target is null");
        TargetDto dto = new TargetDto();
        dto.setId(target.getId());
        dto.setName(target.getName());
        return dto;
    }

    public Target findById(Long id) throws EntityNotFoundException {
        return targetRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Target save(Target target) {
        return targetRepository.save(target);
    }

    public Page<Target> findAllByDeletedTimeIsNull(Pageable pageable) {
        return targetRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
