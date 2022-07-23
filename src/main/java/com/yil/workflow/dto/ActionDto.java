package com.yil.workflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Boolean enabled;
    private Long stepId;
    private Long nextStepId;
    private Long permissionId;
}
