package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.model.Action;
import com.yil.workflow.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


@Service
public class ActionService {

    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public static ActionDto toDto(Action action) throws NullPointerException {
        if (action == null)
            throw new NullPointerException("Action is null");
        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setDescription(action.getDescription());
        dto.setEnabled(action.getEnabled());
        dto.setName(action.getName());
        dto.setPermissionId(action.getPermissionId());
        dto.setStepId(action.getStepId());
        dto.setNextFlowId(action.getNextFlowId());
        dto.setNextStepId(action.getNextStepId());
        dto.setNextGroupId(action.getNextGroupId());
        dto.setNextUserId(action.getNextUserId());
        return dto;
    }

    public Action findById(Long id) throws EntityNotFoundException {
        return actionRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public Action save(Action action) {
        return actionRepository.save(action);
    }

    public Page<Action> findAllByStepIdAndDeletedTimeIsNull(Pageable pageable, Long stepId) {
        return actionRepository.findAllByStepIdAndDeletedTimeIsNull(pageable,stepId);
    }
}
