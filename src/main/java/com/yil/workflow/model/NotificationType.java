package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFS", name = "NOTIFICATION_TYPE")
public class NotificationType implements IEntity {
    @Id
    @SequenceGenerator(name = "NOTIFICATION_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_NOTIFICATION_TYPE_ID",
            schema = "WFS")
    @GeneratedValue(generator = "NOTIFICATION_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
