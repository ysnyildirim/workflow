package com.yil.workflow.service;

import com.yil.workflow.dto.ActionTargetDto;
import com.yil.workflow.model.ActionTarget;
import com.yil.workflow.repository.ActionTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ActionTargetService {

    private final ActionTargetRepository actionTargetRepository;

    @Autowired
    public ActionTargetService(ActionTargetRepository actionTargetRepository) {
        this.actionTargetRepository = actionTargetRepository;
    }

    public static ActionTargetDto toDto(ActionTarget actionTarget) throws NullPointerException {
        if (actionTarget == null)
            throw new NullPointerException("ActionTarget is null");
        ActionTargetDto dto = new ActionTargetDto();
        dto.setId(actionTarget.getId());
        dto.setActionId(actionTarget.getActionId());
        dto.setTargetId(actionTarget.getTargetId());
        dto.setGroupId(actionTarget.getGroupId());
        dto.setUserId(actionTarget.getUserId());
        return dto;
    }

    public ActionTarget findById(Long id) throws EntityNotFoundException {
        return actionTargetRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public ActionTarget save(ActionTarget actionTarget) {
        return actionTargetRepository.save(actionTarget);
    }

    public Page<ActionTarget> findAllByDeletedTimeIsNull(Pageable pageable) {
        return actionTargetRepository.findAllByDeletedTimeIsNull(pageable);
    }

}
