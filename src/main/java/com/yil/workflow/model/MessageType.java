package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "MESSAGE_TYPE")
public class MessageType extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "MESSAGE_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_MESSAGE_TYPE_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "MESSAGE_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
