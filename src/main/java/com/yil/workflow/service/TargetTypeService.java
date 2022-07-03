/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.TargetDto;
import com.yil.workflow.model.TargetType;
import com.yil.workflow.repository.TargetTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TargetTypeService {

    public static final int TaskCreator = 1;
    public static final int LastActionUser = 2;
    public static final int GroupMembers = 3;
    public static final int User = 4;

    private final TargetTypeDao targetTypeDao;

    public static TargetDto toDto(TargetType targetType) {
        if (targetType == null)
            throw new NullPointerException("Step Type is null");
        TargetDto dto = new TargetDto();
        dto.setId(targetType.getId());
        dto.setName(targetType.getName());
        dto.setDescription(targetType.getDescription());
        return dto;
    }

    @Transactional(readOnly = true)
    public boolean existsById(int targetTypeId) {
        return targetTypeDao.existsById(targetTypeId);
    }

}
