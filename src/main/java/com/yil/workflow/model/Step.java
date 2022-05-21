package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Step")
public class Step extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Step_Sequence_Generator",
            sequenceName = "Seq_Step",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Step_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "Descripton", nullable = false, length = 1000)
    private String description;
    @Column(name = "Enabled", nullable = false)
    private Boolean enabled;
    @Column(name = "FlowId")
    private Long flowId;
}
