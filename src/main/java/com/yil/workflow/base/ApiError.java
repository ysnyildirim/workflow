package com.yil.workflow.base;

import lombok.Getter;

@Getter
public enum ApiError {
    ;

    private final int code;
    private final String message;

    ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
