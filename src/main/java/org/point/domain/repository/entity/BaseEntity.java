package org.point.domain.repository.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import java.time.LocalDateTime;

import static org.point.utils.DateUtils.getCurrentLocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = getCurrentLocalDateTime();
        this.modifiedDate = this.createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = getCurrentLocalDateTime();
    }
}
