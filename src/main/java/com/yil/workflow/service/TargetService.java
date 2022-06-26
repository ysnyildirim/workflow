/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.TargetDto;
import com.yil.workflow.model.Target;
import com.yil.workflow.repository.TargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TargetService {

    private final TargetDao targetDao;

    public static TargetDto toDto(Target target) {
        if (target == null)
            throw new NullPointerException("Step Type is null");
        TargetDto dto = new TargetDto();
        dto.setId(target.getId());
        dto.setName(target.getName());
        dto.setDescription(target.getDescription());
        return dto;
    }

}
