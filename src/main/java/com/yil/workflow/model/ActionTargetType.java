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
@Table(schema = "WFS", name = "ACTION_TARGET_TYPE")
public class ActionTargetType implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_TARGET_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_TARGET_TYPE_ID",
            schema = "WFS")
    @GeneratedValue(generator = "ACTION_TARGET_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
