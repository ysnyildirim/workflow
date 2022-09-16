/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.model;

import com.yil.workflow.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "WFS", name = "DOCUMENT")
public class Document implements IEntity {
    @Id
    @Column(name = "HASH_VALUE",length = 32)
    private String hashValue;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false)
    private Byte[] content;
}
