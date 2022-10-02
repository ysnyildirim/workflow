/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.service;

import com.yil.workflow.dto.ActionNotificationTargetTypeDto;
import com.yil.workflow.exception.ActionNotificationTargetTypeNotFoundException;
import com.yil.workflow.model.ActionNotificationTargetType;
import com.yil.workflow.repository.ActionNotificationTargetTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionNotificationTargetTypeService {
    public static ActionNotificationTargetType BelirliBiri;
    public static ActionNotificationTargetType IsiOlusturan;
    public static ActionNotificationTargetType SonIslemYapan;
    public static ActionNotificationTargetType IslemYapan;
    public static ActionNotificationTargetType IslemYapanFarkliSonKisi;
    private final ActionNotificationTargetTypeDao actionNotificationTargetTypeDao;

    public static ActionNotificationTargetTypeDto convert(ActionNotificationTargetType entity) {
        ActionNotificationTargetTypeDto dto = new ActionNotificationTargetTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public ActionNotificationTargetType findById(Integer id) throws ActionNotificationTargetTypeNotFoundException {
        return actionNotificationTargetTypeDao.findById(id).orElseThrow(ActionNotificationTargetTypeNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return actionNotificationTargetTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<ActionNotificationTargetType> findAll() {
        return actionNotificationTargetTypeDao.findAll();
    }
}
