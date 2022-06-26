/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.FlowGroupDto;
import com.yil.workflow.model.FlowGroup;
import com.yil.workflow.repository.FlowGroupDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FlowGroupService {

    private final FlowGroupDao flowGroupDao;

    public static FlowGroupDto toDto(FlowGroup flowGroup) {
        if (flowGroup == null)
            throw new NullPointerException();
        FlowGroupDto dto = new FlowGroupDto();
        dto.setId(flowGroup.getId());
        dto.setName(flowGroup.getName());
        dto.setDescription(flowGroup.getDescription());
        dto.setFlowId(flowGroup.getFlowId());
        return dto;
    }

}
