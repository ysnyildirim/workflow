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
@Table(schema = "WFS", name = "ACTION",
        indexes = {
                @Index(name = "IDX_ACTION_STEP_ID", columnList = "STEP_ID")
        })
public class Action implements IEntity {
    @Id
    @SequenceGenerator(name = "ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_ID",
            schema = "WFS",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
    @Comment(value = "Aksiyonun durumu aktif/pasif")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;
    @Comment(value = "Aksiyonun adımı")
    @Column(name = "STEP_ID", nullable = false)
    private Long stepId;
    @Comment(value = "Aksiyonun sonraki adımı")
    @Column(name = "NEXT_STEP_ID", nullable = false)
    private Long nextStepId;
    @Comment(value = "Kullanıcının Bu aksiyonu kullanabilmesi için gerekli yetki idsi")
    @Column(name = "PERMISSION_ID")
    private Long permissionId;
    @Comment(value = "Oluşturulma tarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    @Comment(value = "Oluşturan kullanıcı id")
    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;
    @Comment(value = "Sonraki kullanıcı hedef türü")
    @Column(name = "ACTION_TARGET_TYPE_ID", nullable = false)
    private Integer actionTargetTypeId;
    @Comment(value = "Sonraki kullanıcı belirli bir kişi")
    @Column(name = "NEXT_USER_ID")
    private Long nextUserId;

}
