package com.yil.workflow.base;

public enum ApiStatus {

    Error(100000, "Error"),
    Ok(100001, "Ok"),
    DisabledUser(100002, "User is disabled"),
    BadCredentials(100003, "Bad creadentials");

    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ApiStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

