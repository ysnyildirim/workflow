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
@Table(schema = "WFS", name = "ACTION_PERMISSION")
public class ActionPermission implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_PERMISSION_ID",
            schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "TARGET_TYPE_ID", nullable = false)
    private Integer targetTypeId;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "USER_ID")
    private Long userId;
}

