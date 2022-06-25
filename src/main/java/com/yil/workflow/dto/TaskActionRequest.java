package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskActionRequest {
    @NotNull
    private Long actionId;
    private TaskActionMessageRequest[] messages;
    private TaskActionDocumentRequest[] documents;
}
