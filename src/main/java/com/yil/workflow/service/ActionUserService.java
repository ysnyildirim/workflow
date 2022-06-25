/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.service;

import com.yil.workflow.model.ActionUser;
import com.yil.workflow.repository.ActionUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ActionUserService {

    private final ActionUserDao actionUserDao;

    public List<ActionUser> findByUserId(Long userId) {
        return actionUserDao.findById_UserId(userId);
    }
}
