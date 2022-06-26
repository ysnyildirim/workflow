/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.FlowGroupUserDto;
import com.yil.workflow.model.FlowGroupUser;
import com.yil.workflow.repository.FlowGroupUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FlowGroupUserService {

    private final FlowGroupUserDao flowGroupUserDao;

    public static FlowGroupUserDto toDto(FlowGroupUser flowGroupUser) {
        if (flowGroupUser == null)
            throw new NullPointerException("Group user is null");
        FlowGroupUserDto dto = new FlowGroupUserDto();
        dto.setUserId(flowGroupUser.getId().getUserId());
        dto.setFlowGroupId(flowGroupUser.getId().getFlowGroupId());
        return dto;
    }

}
