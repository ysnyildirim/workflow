package com.yil.workflow.model;


import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ActionTarget")
public class ActionTarget extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ActionTarget_Sequence_Generator",
            sequenceName = "Seq_ActionTarget",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "ActionTarget_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "ActionId", nullable = false)
    private Long actionId;
    @Column(name = "OrganizationId")
    private Long organizationId;
    @Column(name = "UserId")
    private Long userId;
    @Column(name = "TargetId", nullable = false)
    private Long targetId;
}
