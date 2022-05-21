package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ActionPermission")
public class ActionPermission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ActionPermission_Sequence_Generator",
            sequenceName = "Seq_ActionPermission",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "ActionPermission_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "ActionId", nullable = false)
    private Long actionId;
    @Column(name = "PermissionId", nullable = false)
    private Long permissionId;
}
