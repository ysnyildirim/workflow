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
@Table(schema = "WFS", name = "TASK_ACTION_TARGET")
public class TaskActionTarget implements IEntity {
    @Id
    @SequenceGenerator(name = "TASK_ACTION_TARGET_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TASK_ACTION_TARGET_ID",
            schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "TASK_ACTION_TARGET_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "TASK_ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "GROUP_ID")
    private Long groupId;
    @Column(name = "USER_ID")
    private Long userId;
}

