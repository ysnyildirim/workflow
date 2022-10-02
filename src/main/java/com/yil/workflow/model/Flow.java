package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode
@Entity
@Data
@Table(schema = "WFS", name = "FLOW")
public class Flow implements IEntity {
    @Id
    @SequenceGenerator(name = "FLOW_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_FLOW_ID", schema = "WFS")
    @GeneratedValue(generator = "FLOW_SEQUENCE_GENERATOR")
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME", nullable = false)
    private Date createdTime;
    @Column(name = "CREATED_USER_ID", nullable = false)
    private Long createdUserId;
}
