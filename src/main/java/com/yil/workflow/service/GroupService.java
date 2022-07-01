/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.GroupDto;
import com.yil.workflow.dto.GroupRequest;
import com.yil.workflow.dto.GroupResponse;
import com.yil.workflow.exception.FlowGroupNotFoundException;
import com.yil.workflow.model.Group;
import com.yil.workflow.repository.GroupDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupDao groupDao;

    public static GroupDto toDto(Group group) {
        if (group == null)
            throw new NullPointerException();
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        return dto;
    }

    public Group findByIdAndDeletedTimeIsNull(Long id) throws FlowGroupNotFoundException {
        return groupDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(FlowGroupNotFoundException::new);
    }

    @Transactional
    public GroupResponse save(GroupRequest request, long userId) {
        Group group = new Group();
        return getFlowGroupResponse(request, userId, group);
    }

    private GroupResponse getFlowGroupResponse(GroupRequest request, long userId, Group group) {
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedTime(new Date());
        group.setCreatedUserId(userId);
        group = groupDao.save(group);
        return GroupResponse.builder().id(group.getId()).build();
    }

    @Transactional
    public GroupResponse replace(GroupRequest request, long groupId, long userId) throws FlowGroupNotFoundException {
        Group group = findByIdAndDeletedTimeIsNull(groupId);
        return getFlowGroupResponse(request, userId, group);
    }

}
