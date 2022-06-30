/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.FlowGroupUserDto;
import com.yil.workflow.dto.FlowGroupUserRequest;
import com.yil.workflow.dto.FlowGroupUserResponse;
import com.yil.workflow.model.FlowGroupUser;
import com.yil.workflow.repository.FlowGroupUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public FlowGroupUserResponse save(FlowGroupUserRequest request, long flowGroupId, long userId) {
        FlowGroupUser flowGroupUser = flowGroupUserDao.findById(new FlowGroupUser.Pk(flowGroupId, request.getUserId())).orElse(null);
        if (flowGroupUser == null) {
            flowGroupUser = new FlowGroupUser();
            flowGroupUser.setId(new FlowGroupUser.Pk(flowGroupId, request.getUserId()));
            flowGroupUser = flowGroupUserDao.save(flowGroupUser);
        }
        return FlowGroupUserResponse.builder().userId(flowGroupUser.getId().getUserId()).build();
    }

    public boolean existsById(long id, long userId) {
        return flowGroupUserDao.existsById(new FlowGroupUser.Pk(id,userId));
    }

}
