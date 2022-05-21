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
public class TaskActionDocumentDto {
    private Long id;
    private Long taskActionId;
    private Long documentId;
}
