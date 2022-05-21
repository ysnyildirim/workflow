package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "StepPermission")
public class StepPermission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "StepPermission_Sequence_Generator",
            sequenceName = "Seq_StepPermission",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "StepPermission_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "StepId", nullable = false)
    private Long stepId;
    @Column(name = "PermissionId", nullable = false)
    private Long permissionId;
}
