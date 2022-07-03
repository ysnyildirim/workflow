/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.*;
import com.yil.workflow.exception.GroupNotFoundException;
import com.yil.workflow.exception.GroupUserNotFoundException;
import com.yil.workflow.exception.YouDoNotHavePermissionException;
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
    private final GroupUserService groupUserService;

    public static GroupDto toDto(Group group) {
        if (group == null)
            throw new NullPointerException();
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        return dto;
    }

    @Transactional(rollbackFor = {Throwable.class})
    public GroupResponse save(GroupRequest request, long userId) throws GroupUserNotFoundException, YouDoNotHavePermissionException {
        Group group = new Group();
        GroupResponse groupResponse = getFlowGroupResponse(request, userId, group);
        //grubu oluşturan aynı zamanda grubun adminidir ekle
        GroupUserResponse groupUserResponse = groupUserService.save(GroupUserRequest.builder()
                .userId(userId)
                .groupUserTypeId(GroupUserTypeService.Admin)
                .build(), groupResponse.getId(), userId);
        return groupResponse;
    }

    private GroupResponse getFlowGroupResponse(GroupRequest request, long userId, Group group) {
        group.setName(request.getName());
        group.setDescription(request.getDescription());
        group.setCreatedTime(new Date());
        group.setCreatedUserId(userId);
        group = groupDao.save(group);
        return GroupResponse.builder().id(group.getId()).build();
    }

    /**
     * Sadece grup admini değiştirebilir.
     *
     * @param request
     * @param groupId
     * @param userId
     * @return
     * @throws GroupNotFoundException
     * @throws YouDoNotHavePermissionException
     */
    @Transactional(rollbackFor = {Throwable.class})
    public GroupResponse replace(GroupRequest request, long groupId, long userId) throws GroupNotFoundException, YouDoNotHavePermissionException {
        Group group = findByIdAndDeletedTimeIsNull(groupId);
        if (!groupUserService.isGroupAdmin(groupId, userId))
            throw new YouDoNotHavePermissionException();
        return getFlowGroupResponse(request, userId, group);
    }

    @Transactional(readOnly = true)
    public Group findByIdAndDeletedTimeIsNull(Long id) throws GroupNotFoundException {
        return groupDao.findByIdAndDeletedTimeIsNull(id).orElseThrow(GroupNotFoundException::new);
    }

    /**
     * Grubu sadece adminleri silebilir.
     *
     * @param groupId
     * @param userId
     * @throws YouDoNotHavePermissionException
     */
    @Transactional(rollbackFor = {Throwable.class})
    public void delete(long groupId, long userId) throws YouDoNotHavePermissionException {
        if (!groupUserService.isGroupAdmin(groupId, userId))
            throw new YouDoNotHavePermissionException();
        groupDao.deleteById(groupId);
        groupUserService.deleteByGroupId(groupId, userId);
    }

    public boolean existsById(Long groupId) {
        return groupDao.existsById(groupId);
    }
}
