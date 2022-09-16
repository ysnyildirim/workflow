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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(schema = "WFS",
        name = "ACTION_NOTIFICATION",
        indexes = {
                @Index(name = "IDX_ACTION_NOTIFICATION_ACTION_ID", columnList = "ACTION_ID")
        })
public class ActionNotification implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_NOTIFICATION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_NOTIFICATION_ID",
            schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_NOTIFICATION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
    @Column(name = "SUBJECT", nullable = false, length = 100)
    private String subject;
    @Lob
    @Column(name = "MESSAGE", nullable = false)
    private String message;
}
