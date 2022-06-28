/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.FlowGroupDto;
import com.yil.workflow.dto.FlowGroupRequest;
import com.yil.workflow.dto.FlowGroupResponse;
import com.yil.workflow.exception.FlowGroupNotFoundException;
import com.yil.workflow.model.FlowGroup;
import com.yil.workflow.repository.FlowGroupDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    public FlowGroup findByIdAndDeletedTimeIsNull(Long id) throws FlowGroupNotFoundException {
        return flowGroupDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowGroupNotFoundException::new);
    }

    public List<FlowGroup> findAllByFlowIdAndDeletedTimeIsNull(Long flowId) {
        return flowGroupDao.findAllByFlowIdAndDeletedTimeIsNull(flowId);
    }

    @Transactional
    public FlowGroupResponse save(FlowGroupRequest request, long flowId, long userId) {
        FlowGroup flowGroup = new FlowGroup();
        flowGroup.setFlowId(flowId);
        return getFlowGroupResponse(request, userId, flowGroup);
    }

    private FlowGroupResponse getFlowGroupResponse(FlowGroupRequest request, long userId, FlowGroup flowGroup) {
        flowGroup.setName(request.getName());
        flowGroup.setDescription(request.getDescription());
        flowGroup.setFlowGroupTypeId(request.getGroupTypeId());
        flowGroup.setCreatedTime(new Date());
        flowGroup.setCreatedUserId(userId);
        flowGroup = flowGroupDao.save(flowGroup);
        return FlowGroupResponse.builder().id(flowGroup.getId()).build();
    }

    @Transactional
    public FlowGroupResponse replace(FlowGroupRequest request, long flowGroupId, long userId) throws FlowGroupNotFoundException {
        FlowGroup flowGroup = findByIdAndDeletedTimeIsNull(flowGroupId);
        return getFlowGroupResponse(request, userId, flowGroup);
    }

}
