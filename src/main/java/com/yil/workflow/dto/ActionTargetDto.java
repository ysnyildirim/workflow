package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionTargetDto {
    private Long id;
    private Long actionId;
    private Long groupId;
    private Long userId;
    private Long targetId;
}
