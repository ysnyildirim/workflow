/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.exception;

import com.yil.workflow.base.ApiException;
import com.yil.workflow.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@ApiException(code = ErrorCode.YouDoNotHavePermission)
public class YouDoNotHavePermissionException extends Exception {

}
