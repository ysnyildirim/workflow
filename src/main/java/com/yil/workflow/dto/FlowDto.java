package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowDto {
    private Long id;
    private String name;
    private String description;
    private Boolean enabled;
    private Long startUpStepId;
    private Long startUpPermissionId;
}
