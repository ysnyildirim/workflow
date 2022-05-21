package com.yil.workflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDto {
    @NotNull
    private Long flowId;
    @NotNull
    private Date startDate;
    private Date finishDate;
    private Date estimatedFinishDate;
    private Long currentTaskActionId;
    @NotNull
    private Long taskStatusId;
}
