package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Action")
public class Action extends AbstractEntity {

    @Id
    @SequenceGenerator(name = "Action_Sequence_Generator",
            sequenceName = "Seq_Action",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Action_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "Descripton", nullable = false, length = 1000)
    private String description;
    @Column(name = "Enabled", nullable = false)
    private Boolean enabled;
    @Column(name = "StepId", nullable = false)
    private Long stepId;
    @Column(name = "NextStepId", nullable = false)
    private Long nextStepId;

}
