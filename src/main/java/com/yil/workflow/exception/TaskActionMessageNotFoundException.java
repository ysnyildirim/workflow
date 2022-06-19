/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.exception;

import com.yil.workflow.base.ApiException;
import com.yil.workflow.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ApiException(code = ErrorCode.TaskActionMessageNotFound)
public class TaskActionMessageNotFoundException extends Exception {
}
