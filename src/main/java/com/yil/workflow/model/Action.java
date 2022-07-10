package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(schema = "WFS", name = "ACTION")
public class Action extends AbstractEntity {
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
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "1")
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;
    /**
     * Aksiyonun adımı
     */
    @Column(name = "STEP_ID", nullable = false)
    private Long stepId;
    /**
     * Aksiyonun sonraki adımı
     */
    @Column(name = "NEXT_STEP_ID", nullable = false)
    private Long nextStepId;
    @Column(name = "TARGET_TYPE_ID", nullable = false)
    private Integer targetTypeId;
    @Column(name = "NEXT_GROUP_ID")
    private Long nextGroupId;
    @Column(name = "NEXT_USER_ID")
    private Long nextUserId;
}
