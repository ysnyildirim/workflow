package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "TaskActionDocument")
public class TaskActionDocument extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "TaskActionDocument_Sequence_Generator",
            sequenceName = "Seq_TaskActionDocument_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "TaskActionDocument_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "TaskActionId", nullable = false)
    private Long taskActionId;
    @Column(name = "DocumentId", nullable = false)
    private Long documentId;
}
