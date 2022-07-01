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
        dto.setUserId(groupUser.getId().getUserId());
        dto.setGroupId(groupUser.getId().getGroupId());
        dto.setGroupUserTypeId(groupUser.getId().getGroupUserTypeId());
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
        GroupUser groupUser = groupUserDao.findById(new GroupUser.Pk(groupId, request.getUserId(), request.getGroupUserTypeId())).orElse(null);
        if (groupUser == null) {
            groupUser = new GroupUser();
            groupUser.setId(new GroupUser.Pk(groupId, request.getUserId(), request.getGroupUserTypeId()));
            groupUser = groupUserDao.save(groupUser);
        }
        return GroupUserResponse.builder().userId(groupUser.getId().getUserId()).build();
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

    @Transactional(readOnly = true)
    public boolean existsByIdIn(List<GroupUser.Pk> id) {
        return groupUserDao.existsByIdIn(id);
    }

    @Transactional(readOnly = true)
    public long countByGroupId(long groupId) {
        return groupUserDao.countById_GroupId(groupId);
    }

    @Transactional(readOnly = true)
    public boolean existsById(GroupUser.Pk id) {
        return groupUserDao.existsById(id);
    }

    public boolean isGroupAdmin(long groupId, long userId) {
        return existsById(GroupUser.Pk.builder().userId(userId).groupId(groupId).groupUserTypeId(1).build());
    }

    public boolean isGroupManager(long groupId, long userId) {
        return existsById(GroupUser.Pk.builder().userId(userId).groupId(groupId).groupUserTypeId(2).build());
    }

    public boolean isGroupUser(long groupId, long userId) {
        return existsById(GroupUser.Pk.builder().userId(userId).groupId(groupId).groupUserTypeId(3).build());
    }


    /**
     * Grouptan sadece admin veya manager silme yapabilir
     *
     * @param groupId
     */
    @Transactional(rollbackFor = {Throwable.class})
    public void deleteByGroupId(long groupId, long userId) throws YouDoNotHavePermissionException {
        if (!canDeleta(groupId, userId))
            throw new YouDoNotHavePermissionException();
        groupUserDao.deleteById_GroupId(groupId);
    }

    /**
     * admin silebilir
     *
     * @param groupId
     * @param userId
     * @return
     */
    public boolean canDeleta(long groupId, long userId) {
        return isGroupAdmin(groupId, userId);
    }

}
