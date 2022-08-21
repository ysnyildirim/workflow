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

@Entity
@Data
@Table(schema = "WFS", name = "STEP",
        indexes = {
                @Index(name = "IDX_STEP_FLOW_ID", columnList = "FLOW_ID"),
                @Index(name = "IDX_STEP_STEP_TYPE_ID", columnList = "STEP_TYPE_ID")
        })
public class Step implements IEntity {
    @Id
    @SequenceGenerator(name = "STEP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STEP_ID",
            schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "STEP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;
    @Column(name = "FLOW_ID", nullable = false)
    private Long flowId;
    @Column(name = "STATUS_ID", nullable = false)
    private Integer statusId;
    @Comment("Adım türü")
    @Column(name = "STEP_TYPE_ID", nullable = false)
    private Integer stepTypeId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;
    @Comment("Adımda mesaj eklenebilir mi?")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "CAN_ADD_DOCUMENT", nullable = false)
    private boolean canAddDocument;
    @Comment("Adımda döküman eklenebilir mi?")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "CAN_ADD_MESSAGE", nullable = false)
    private boolean canAddMessage;
}
