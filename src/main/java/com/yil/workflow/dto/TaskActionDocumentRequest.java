package com.yil.workflow.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskActionDocumentRequest {
    @NonNull
    private Byte[] content;
    @NotBlank
    private String name;
    @NotBlank
    private String extension;
    @NonNull
    private Date uploadedDate;
}
