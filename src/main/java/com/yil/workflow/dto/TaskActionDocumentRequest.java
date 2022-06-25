package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskActionDocumentRequest {
    private Byte[] content;
    private String name;
    private String extension;
    private Date uploadedDate;
}
