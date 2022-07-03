/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.GroupUserDto;
import com.yil.workflow.dto.GroupUserRequest;
import com.yil.workflow.dto.GroupUserResponse;
import com.yil.workflow.exception.GroupUserNotFoundException;
import com.yil.workflow.exception.YouDoNotHavePermissionException;
import com.yil.workflow.model.GroupUser;
import com.yil.workflow.repository.GroupUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupUserService {

    private final GroupUserDao groupUserDao;
    private final GroupUserTypeService groupUserTypeService;

    public static GroupUserDto toDto(GroupUser groupUser) {
        if (groupUser == null)
            throw new NullPointerException("Group user is null");
        GroupUserDto dto = new GroupUserDto();
        dto.setId(groupUser.getId());
        dto.setUserId(groupUser.getUserId());
        dto.setGroupId(groupUser.getGroupId());
        dto.setGroupUserTypeId(groupUser.getGroupUserTypeId());
        return dto;
    }

    /**
     * Gruba sadece admin veya manager ekleme yapabilir.
     *
     * @param request
     * @param groupId
     * @param userId
     * @return
     * @throws GroupUserNotFoundException
     */
    @Transactional(rollbackFor = {Throwable.class})
    public GroupUserResponse save(GroupUserRequest request, long groupId, long userId) throws GroupUserNotFoundException, YouDoNotHavePermissionException {
        if (!canAdd(groupId, userId))
            throw new YouDoNotHavePermissionException();
        if (!groupUserTypeService.existsById(request.getGroupUserTypeId()))
            throw new GroupUserNotFoundException();
        List<GroupUser> groupUsers = groupUserDao.findAllByGroupIdAndUserIdAndGroupUserTypeId(groupId, request.getUserId(), request.getGroupUserTypeId());
        GroupUser groupUser = groupUsers.stream().findFirst().orElse(null);
        if (groupUser == null) {
            groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(request.getUserId());
            groupUser.setGroupUserTypeId(request.getGroupUserTypeId());
            groupUser = groupUserDao.save(groupUser);
        }
        return GroupUserResponse.builder().id(groupUser.getId()).build();
    }

    /**
     * admin veya manager ekleyebilir
     * yada grup boşsa ekleyebilir (yeni grup oluşturmada kural geçsin)
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean canAdd(long groupId, long userId) {
        if (isGroupAdmin(groupId, userId) || isGroupManager(groupId, userId))
            return true;
        return (countByGroupId(groupId) == 0); // grupta kimse yoksa ekleyebilir
    }

    public boolean isGroupAdmin(long groupId, long userId) {
        return groupUserDao.existsByGroupIdAndUserIdAndGroupUserTypeId(groupId, userId, 1);
    }

    public boolean isGroupManager(long groupId, long userId) {
        return groupUserDao.existsByGroupIdAndUserIdAndGroupUserTypeId(groupId, userId, 2);
    }

    @Transactional(readOnly = true)
    public long countByGroupId(long groupId) {
        return groupUserDao.countByGroupId(groupId);
    }

    public boolean isGroupUser(long groupId, long userId) {
        return groupUserDao.existsByGroupIdAndUserIdAndGroupUserTypeId(groupId, userId, 3);
    }

    /**
     * Grouptan sadece admin veya manager silme yapabilir
     *
     * @param groupId
     */
    @Transactional(rollbackFor = {Throwable.class})
    public void deleteByGroupId(long groupId, long userId) throws YouDoNotHavePermissionException {
        if (!canDelete(groupId, userId))
            throw new YouDoNotHavePermissionException();
        groupUserDao.deleteByGroupId(groupId);
    }

    /**
     * admin silebilir
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean canDelete(long groupId, long userId) {
        return isGroupAdmin(groupId, userId);
    }

    @Transactional(readOnly = true)
    public List<GroupUser> getManagers(Long groupId) {
        return groupUserDao.findAllByGroupIdAndGroupUserTypeId(groupId, GroupUserTypeService.Manager);
    }
}
