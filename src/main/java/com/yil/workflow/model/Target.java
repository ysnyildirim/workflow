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

/**
 * 1- Request Creator (Requester)
 * 2- Request Stakeholders
 * 3- Group Members
 * 4- Process Admins
 */
@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "TARGET")
public class Target implements IEntity {
    @Id
    @SequenceGenerator(name = "TARGET_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_TARGET_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "TARGET_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
