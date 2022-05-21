package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "TaskStatus")
public class TaskStatus extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TaskStatus_Sequence_Generator",
            sequenceName = "Seq_TaskStatus",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TaskStatus_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
}
