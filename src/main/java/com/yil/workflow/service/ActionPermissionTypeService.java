/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.ActionPermissionTypeDto;
import com.yil.workflow.exception.ActionPermissionTypeNotFoundException;
import com.yil.workflow.model.ActionPermissionType;
import com.yil.workflow.repository.ActionPermissionTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
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

    @Transactional(readOnly = true)
    public ActionPermissionType findById(Integer id) throws ActionPermissionTypeNotFoundException {
        return actionPermissionTypeDao.findById(id).orElseThrow(ActionPermissionTypeNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return actionPermissionTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<ActionPermissionType> findAll() {
        return actionPermissionTypeDao.findAll();
    }
}
