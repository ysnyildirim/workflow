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
    @Column(name = "CreatedTime")
    private Date createdTime;
    @Column(name = "CreatedUserId")
    private Long createdUserId;
    @Column(name = "DeletedUserId")
    private Long deletedUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DeletedTime")
    private Date deletedTime;
}
