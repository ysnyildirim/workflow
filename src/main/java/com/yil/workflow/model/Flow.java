package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Flow")
public class Flow extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Flow_Sequence_Generator",
            sequenceName = "Seq_Flow_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Flow_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "Descripton", nullable = false, length = 1000)
    private String description;
    @Column(name = "Enabled", nullable = false)
    private Boolean enabled;
    @Column(name = "StartUpStepId", nullable = false)
    private Long startUpStepId;
    @Column(name = "StartUpPermissionId")
    private Long startUpPermissionId;
}
