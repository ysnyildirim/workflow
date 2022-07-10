/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.exception.TargetNotFoundException;
import com.yil.workflow.model.Target;
import com.yil.workflow.repository.TargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TargetService {

    private final TargetDao targetDao;

    public Target findById(long id) throws TargetNotFoundException {
        return targetDao.findById(id).orElseThrow(TargetNotFoundException::new);
    }

}
