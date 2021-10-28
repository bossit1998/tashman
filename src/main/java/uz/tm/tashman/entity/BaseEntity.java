package uz.tm.tashman.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(columnDefinition = "boolean default false")
    protected boolean deleted = false;

    protected LocalDateTime deletedDate;

    public abstract Long getId();
}