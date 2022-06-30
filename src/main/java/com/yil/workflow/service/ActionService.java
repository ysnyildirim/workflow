package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponse;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.model.Action;
import com.yil.workflow.repository.ActionDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ActionService {

    private final ActionDao actionDao;
    private final StepService stepService;

    public static ActionDto toDto(Action action) throws NullPointerException {
        if (action == null)
            throw new NullPointerException("Action is null");
        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setDescription(action.getDescription());
        dto.setEnabled(action.getEnabled());
        dto.setName(action.getName());
        dto.setStepId(action.getStepId());
        dto.setNextStepId(action.getNextStepId());
        return dto;
    }

    public Action findById(Long id) throws ActionNotFoundException {
        return actionDao.findById(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndStepId(Long id, Long stepId) throws ActionNotFoundException {
        return actionDao.findByIdAndStepId(id, stepId).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id, Long stepId) throws ActionNotFoundException {
        return actionDao.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    @Transactional
    public ActionResponse save(ActionRequest request, Long stepId, Long userId) throws StepNotFoundException {
        if (!stepService.existsById(stepId))
            throw new StepNotFoundException();
        Action action = new Action();
        action.setStepId(stepId);
        return getActionResponce(request, userId, action);
    }

    @Transactional
    public ActionResponse replace(ActionRequest request, Long actionId, Long userId) throws ActionNotFoundException, StepNotFoundException {
        Action action = findByIdAndEnabledTrueAndDeletedTimeIsNull(actionId);
        return getActionResponce(request, userId, action);
    }

    public ActionResponse getActionResponce(ActionRequest request, Long userId, Action action) throws StepNotFoundException {
        if (!stepService.existsById(request.getNextStepId()))
            throw new StepNotFoundException();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setCreatedUserId(userId);
        action.setCreatedTime(new Date());
        action = actionDao.save(action);
        return ActionResponse.builder().id(action.getId()).build();
    }

    @Transactional
    public void delete(Long actionId, Long userId) throws ActionNotFoundException {
        Action action = findByIdAndDeletedTimeIsNull(actionId);
        action.setDeletedUserId(userId);
        action.setDeletedTime(new Date());
        actionDao.save(action);
    }

    public List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId) {
        return actionDao.findAllByStepIdAndDeletedTimeIsNull(stepId);
    }
}
