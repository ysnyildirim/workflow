/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.dto.ActionTargetTypeDto;
import com.yil.workflow.exception.ActionTargetTypeNotFoundException;
import com.yil.workflow.model.ActionTargetType;
import com.yil.workflow.repository.ActionTargetTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ActionTargetTypeService {

    public static ActionTargetType Ozel;
    public static ActionTargetType BelirliBiri;
    public static ActionTargetType Olusturan;
    public static ActionTargetType SonIslemYapan;
    public static ActionTargetType IslemYapan;
    public static ActionTargetType IslemYapanFarkliSonKisi;

    private final ActionTargetTypeDao actionTargetTypeDao;

    public static ActionTargetTypeDto convert(ActionTargetType actionTargetType) {
        ActionTargetTypeDto dto = new ActionTargetTypeDto();
        dto.setId(actionTargetType.getId());
        dto.setName(actionTargetType.getName());
        return dto;
    }

    @Transactional(readOnly = true)
    public ActionTargetType findById(Integer id) throws ActionTargetTypeNotFoundException {
        return actionTargetTypeDao.findById(id).orElseThrow(ActionTargetTypeNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return actionTargetTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<ActionTargetType> findAll() {
        return actionTargetTypeDao.findAll();
    }
}
