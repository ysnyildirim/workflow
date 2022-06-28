/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "ACTION_TARGET")
public class ActionTarget implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_TARGET_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_TARGET_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_TARGET_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "TARGET_ID", nullable = false)
    private Integer targetId;
    @Column(name = "FLOW_GROUP_ID")
    private Long flowGroupId;
}
