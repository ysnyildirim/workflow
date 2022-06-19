package com.yil.workflow.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements IEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
    @Column(name = "DELETED_USER_ID")
    private Long deletedUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELETED_TIME")
    private Date deletedTime;
}

