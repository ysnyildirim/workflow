package com.yil.workflow.service;

import com.yil.workflow.dto.ActionDto;
import com.yil.workflow.dto.ActionRequest;
import com.yil.workflow.dto.ActionResponce;
import com.yil.workflow.exception.ActionNotFoundException;
import com.yil.workflow.exception.ActionTypeNotFoundException;
import com.yil.workflow.exception.StepNotFoundException;
import com.yil.workflow.model.Action;
import com.yil.workflow.repository.ActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final StepService stepService;
    private final ActionTypeService actionTypeService;

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
        dto.setActionTypeId(action.getActionTypeId());
        return dto;
    }

    public Action findById(Long id) throws ActionNotFoundException {
        return actionRepository.findById(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndStepId(Long id, Long stepId) throws ActionNotFoundException {
        return actionRepository.findByIdAndStepId(id, stepId).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndEnabledTrueAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionRepository.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndStepIdAndEnabledTrueAndDeletedTimeIsNotNull(Long id, Long stepId) throws ActionNotFoundException {
        return actionRepository.findByIdAndEnabledTrueAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        return actionRepository.findByIdAndDeletedTimeIsNull(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public boolean availableAction(long id, long userId) {
        //bu action nextuser veya grup bu kullanıcı mı ?
        boolean existsGroupOrUser = true;
        if (!existsGroupOrUser) {
            //bu kişi bu işlemi yapamaz
            return false;
        }
        //action yetkisi var mı ?
        boolean existsActionPermission = true;
        //execute yetkisi yok
        return existsActionPermission;
    }

    @Transactional
    public ActionResponce save(ActionRequest request, Long stepId, Long userId) throws StepNotFoundException, ActionTypeNotFoundException {
        if (!stepService.existsById(stepId))
            throw new StepNotFoundException();
        Action action = new Action();
        action.setStepId(stepId);
        return getActionResponce(request, userId, action);
    }

    @Transactional
    public ActionResponce replace(ActionRequest request, Long actionId, Long userId) throws ActionNotFoundException, StepNotFoundException, ActionTypeNotFoundException {
        Action action = findByIdAndEnabledTrueAndDeletedTimeIsNull(actionId);
        return getActionResponce(request, userId, action);
    }

    public ActionResponce getActionResponce(ActionRequest request, Long userId, Action action) throws StepNotFoundException, ActionTypeNotFoundException {
        if (!stepService.existsById(request.getNextStepId()))
            throw new StepNotFoundException();
        if (!actionTypeService.existsById(request.getActionTypeId()))
            throw new ActionTypeNotFoundException();
        action.setName(request.getName());
        action.setDescription(request.getDescription());
        action.setEnabled(request.getEnabled());
        action.setNextStepId(request.getNextStepId());
        action.setActionTypeId(request.getActionTypeId());
        action.setCreatedUserId(userId);
        action.setCreatedTime(new Date());
        action = actionRepository.save(action);
        return ActionResponce.builder().id(action.getId()).build();
    }

    @Transactional
    public void delete(Long actionId, Long userId) throws ActionNotFoundException {
        Action action = findByIdAndDeletedTimeIsNull(actionId);
        action.setDeletedUserId(userId);
        action.setDeletedTime(new Date());
        actionRepository.save(action);
    }

    public List<Action> findAllByStepIdAndDeletedTimeIsNull(Long stepId) {
        return actionRepository.findAllByStepIdAndDeletedTimeIsNull(stepId);
    }
}
