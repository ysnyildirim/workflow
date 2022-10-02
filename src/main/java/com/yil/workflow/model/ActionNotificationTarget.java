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
@Table(schema = "WFS", name = "ACTION_NOTIFICATION_TARGET")
public class ActionNotificationTarget implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_NOTIFICATION_TARGET_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_NOTIFICATION_TARGET_ID",
            schema = "WFS")
    @GeneratedValue(generator = "ACTION_NOTIFICATION_TARGET_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACTION_NOTIFICATION_ID", nullable = false)
    private Long actionNotificationId;
    @Column(name = "ACTION_NOTIFICATION_TARGET_TYPE_ID", nullable = false)
    private Integer actionNotificationTargetTypeId;
    @Column(name = "USER_ID")
    private Long userId;
}
