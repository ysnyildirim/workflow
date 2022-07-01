/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.GroupUserDto;
import com.yil.workflow.dto.GroupUserRequest;
import com.yil.workflow.dto.GroupUserResponse;
import com.yil.workflow.exception.GroupUserNotFoundException;
import com.yil.workflow.model.GroupUser;
import com.yil.workflow.repository.GroupUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GroupUserService {

    private final GroupUserDao groupUserDao;
    private final GroupUserTypeService groupUserTypeService;

    public static GroupUserDto toDto(GroupUser groupUser) {
        if (groupUser == null)
            throw new NullPointerException("Group user is null");
        GroupUserDto dto = new GroupUserDto();
        dto.setUserId(groupUser.getId().getUserId());
        dto.setGroupId(groupUser.getId().getGroupId());
        dto.setGroupUserTypeId(groupUser.getId().getGroupUserTypeId());
        return dto;
    }

    @Transactional
    public GroupUserResponse save(GroupUserRequest request, long groupId, long userId) throws GroupUserNotFoundException {
        if (!groupUserTypeService.existsById(request.getGroupUserTypeId()))
            throw new GroupUserNotFoundException();
        GroupUser groupUser = groupUserDao.findById(new GroupUser.Pk(groupId, request.getUserId(), request.getGroupUserTypeId())).orElse(null);
        if (groupUser == null) {
            groupUser = new GroupUser();
            groupUser.setId(new GroupUser.Pk(groupId, request.getUserId(), request.getGroupUserTypeId()));
            groupUser = groupUserDao.save(groupUser);
        }
        return GroupUserResponse.builder().userId(groupUser.getId().getUserId()).build();
    }

    public boolean existsById(GroupUser.Pk id) {
        return groupUserDao.existsById(id);
    }

}
