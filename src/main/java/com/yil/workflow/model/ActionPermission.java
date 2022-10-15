/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "WFS", name = "ACTION_PERMISSION",
        indexes = {
                @Index(name = "IDX_ACTION_PERMISSION", columnList = "ACTION_ID,ACTION_PERMISSION_TYPE_ID")
        })
public class ActionPermission implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_PERMISSION_ID",
            schema = "WFS")
    @GeneratedValue(generator = "ACTION_PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "ACTION_PERMISSION_TYPE_ID", nullable = false)
    private Integer actionPermissionTypeId;
    @Comment(value = "Kullanıcının Bu aksiyonu kullanabilmesi için gerekli yetki idsi")
    @Column(name = "PERMISSION_ID")
    private Long permissionId;
}
