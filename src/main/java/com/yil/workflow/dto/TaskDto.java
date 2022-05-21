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
public class TaskDto {
    private Long id;
    private Long flowId;
    private Date startDate;
    private Date finishDate;
    private Date estimatedFinishDate;
    private Long currentTaskActionId;
    private Long taskStatusId;
}
