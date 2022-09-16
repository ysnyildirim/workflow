/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ActionNotificationTargetTypeNotFound(7000022, "Bu bildirim türü bulunamadı!"),
    ActionTargetTypeNotFound(7000021, "Bu aksiyon hedef türü bulunamadı!"),
    ActionPermissionTypeNotFound(7000020, "Bu aksiyon yetki türü bulunamadı!"),
    CannotBeAddedToThisStep(7000019, "Bu aksiyon bu adıma eklenemez"),
    NotNextAction(7000018, "Not next action"),
    StartUpAction(7000017, "Not in action initialization step"),
    StepTypeNotFound(7000013, "Step type not found"),
    YouDoNotHavePermission(7000011, "You do not have permission"),
    TaskNotFound(7000009, "Task not found"),
    TaskActionMessageNotFound(7000008, "Task action message not found"),
    TaskActionDocumentNotFound(7000007, "Task action document not found"),
    TaskActionNotFound(7000006, "Task action not found"),
    StepNotFound(7000005, "Step not found"),
    StatusNotFound(7000004, "Status not found"),
    PriorityNotFound(7000003, "Priority not found"),
    FlowNotFound(7000002, "Flow not found"),
    ActionNotFound(7000000, "Action not found");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
