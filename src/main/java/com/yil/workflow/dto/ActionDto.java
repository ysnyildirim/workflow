package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionDto {
    private Long id;
    private String name;
    private String description;
    private Boolean enabled;
    private Long stepId;
    private Long nextStepId;
    private Long permissionId;
}
