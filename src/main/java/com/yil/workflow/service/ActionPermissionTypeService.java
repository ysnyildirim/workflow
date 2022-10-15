/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.ActionPermissionTypeDto;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermissionType;
import com.yil.workflow.repository.ActionPermissionTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ActionPermissionTypeService {
    public static ActionPermissionType Herkes;
    public static ActionPermissionType Atanan;
    public static ActionPermissionType Olusturan;
    public static ActionPermissionType SonIslemYapan;
    public static ActionPermissionType IslemYapanlar;
    public static ActionPermissionType YetkisiOlan;
    private final ActionPermissionTypeDao actionPermissionTypeDao;

    public static ActionPermissionTypeDto convert(ActionPermissionType actionPermissionType) {
        ActionPermissionTypeDto dto = new ActionPermissionTypeDto();
        dto.setId(actionPermissionType.getId());
        dto.setName(actionPermissionType.getName());
        return dto;
    }

    @Cacheable(value = "actionPermissionTypes", key = "#id")
    public ActionPermissionType findById(Integer id) throws ActionPermissionTypeNotFoundException {
        return actionPermissionTypeDao.findById(id).orElseThrow(ActionPermissionTypeNotFoundException::new);
    }

    @Cacheable(value = "actionPermissionTypes_exists", key = "#id")
    public boolean existsById(Integer id) {
        return actionPermissionTypeDao.existsById(id);
    }

    @Cacheable(value = "actionPermissionTypes")
    public List<ActionPermissionType> findAll() {
        return actionPermissionTypeDao.findAll();
    }
}
