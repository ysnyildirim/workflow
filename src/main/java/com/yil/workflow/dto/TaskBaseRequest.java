/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskBaseRequest {
    @NotNull
    private Date startDate;
    private Date finishDate;
    private Date estimatedFinishDate;
    @NotNull
    private Integer priorityTypeId;
}
