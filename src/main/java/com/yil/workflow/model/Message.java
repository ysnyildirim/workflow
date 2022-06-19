package com.yil.workflow.model;

import com.yil.workflow.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "MESSAGE")
public class Message extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "MESSAGE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_MESSAGE_ID",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "MESSAGE_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "TİTLE", nullable = false, length = 100)
    private String title;
    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;
}
