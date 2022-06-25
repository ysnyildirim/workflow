package com.yil.workflow.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskActionDocumentDto {
    private Long id;
    private Long documentId;
    private Long taskActionId;
    private String name;
    private String extension;
    private Date uploadedDate;
}
