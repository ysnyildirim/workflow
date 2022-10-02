/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "WFS", name = "NOTIFICATION")
public class Notification implements IEntity {
    @Id
    @SequenceGenerator(name = "NOTIFICATION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_NOTIFICATION_ID", schema = "WFS")
    @GeneratedValue(generator = "NOTIFICATION_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "SUBJECT", nullable = false, length = 100)
    private String subject;
    @Lob
    @Column(name = "MESSAGE", nullable = false, length = 4000)
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;
    @Comment(value = "Okunma durumu")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "READ", nullable = false)
    private boolean read;
    @Comment(value = "Bildirim türü")
    @Column(name = "NOTIFICATION_TYPE_ID", nullable = false)
    private Integer notificationTypeId;
}
