package com.yil.workflow.service;

import com.yil.workflow.dto.GroupUserTypeDto;
import com.yil.workflow.exception.GroupUserNotFoundException;
import com.yil.workflow.model.GroupUserType;
import com.yil.workflow.repository.GroupUserTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupUserTypeService {

    public static final int Admin = 1;
    public static final int Manager = 2;
    public static final int User = 3;

    private final GroupUserTypeDao groupUserTypeDao;

    public static GroupUserTypeDto toDto(GroupUserType groupUserType) throws NullPointerException {
        if (groupUserType == null)
            throw new NullPointerException("TaskGroupUser is null");
        GroupUserTypeDto dto = new GroupUserTypeDto();
        dto.setId(groupUserType.getId());
        dto.setName(groupUserType.getName());
        dto.setDescription(groupUserType.getDescription());
        return dto;
    }

    @Transactional(readOnly = true)
    public GroupUserType findById(Integer id) throws GroupUserNotFoundException {
        return groupUserTypeDao.findById(id).orElseThrow(() -> {
            return new GroupUserNotFoundException();
        });
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return groupUserTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<GroupUserType> findAll() {
        return groupUserTypeDao.findAll();
    }

}
