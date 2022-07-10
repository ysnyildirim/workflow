/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.repository.ActionTargetDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ActionTargetService {

    private final ActionTargetDao actionTargetDao;

    public boolean existsByActionIdAndTargetTypeId(long actionId, int targetTypeId) {
        return existsByActionIdAndTargetTypeId(actionId, targetTypeId);
    }

}
