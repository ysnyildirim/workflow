/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionTypeDto;
import com.yil.workflow.exception.ActionTypeNotFoundException;
import com.yil.workflow.model.ActionType;
import com.yil.workflow.repository.ActionTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionTypeService {

    private final ActionTypeDao actionTypeDao;

    public static ActionTypeDto toDto(ActionType actionType) {
        if (actionType == null)
            throw new NullPointerException("Action Type is null");
        ActionTypeDto dto = new ActionTypeDto();
        dto.setId(actionType.getId());
        dto.setName(actionType.getName());
        dto.setDescription(actionType.getDescription());
        return dto;
    }

    public ActionType findById(Integer id) throws ActionTypeNotFoundException {
        return actionTypeDao.findById(id).orElseThrow(() -> new ActionTypeNotFoundException());
    }

    public List<ActionType> findAll() {
        return actionTypeDao.findAll();
    }

    public boolean existsById(Integer actionTypeId) {
        return actionTypeDao.existsById(actionTypeId);
    }
}
