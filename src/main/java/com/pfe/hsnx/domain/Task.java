package com.pfe.hsnx.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Task.
 */
@Entity
@Table(name = "TblTask")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "TaskID")
    private Long id;

    @NotNull
    @Column(name = "TaskTitle", nullable = false)
    private String tasktitle;

    @Column(name = "TaskDescription")
    private String taskdescription;

    @Column(name = "DateStart")
    private LocalDate dateStart;

    @Column(name = "TimeStart")
    private Instant timeStart;

    @Column(name = "DateEnd")
    private LocalDate dateEnd;

    @Column(name = "TimeEnd")
    private Instant timeEnd;

    @Column(name = "TaskStatus")
    private Integer taskstatus;

    @Column(name = "TaskPriority")
    private Integer taskpriority;

    @Column(name = "DueDate")
    private LocalDate DueDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTasktitle() {
        return tasktitle;
    }

    public Task tasktitle(String tasktitle) {
        this.tasktitle = tasktitle;
        return this;
    }

    public void setTasktitle(String tasktitle) {
        this.tasktitle = tasktitle;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public Task taskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
        return this;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public Task dateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getTimeStart() {
        return timeStart;
    }

    public Task timeStart(Instant timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public Task dateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Instant getTimeEnd() {
        return timeEnd;
    }

    public Task timeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getTaskstatus() {
        return taskstatus;
    }

    public Task taskstatus(Integer taskstatus) {
        this.taskstatus = taskstatus;
        return this;
    }

    public void setTaskstatus(Integer taskstatus) {
        this.taskstatus = taskstatus;
    }

    public Integer getTaskpriority() {
        return taskpriority;
    }

    public Task taskpriority(Integer taskpriority) {
        this.taskpriority = taskpriority;
        return this;
    }

    public void setTaskpriority(Integer taskpriority) {
        this.taskpriority = taskpriority;
    }

    public LocalDate getDueDate() {
        return DueDate;
    }

    public Task DueDate(LocalDate DueDate) {
        this.DueDate = DueDate;
        return this;
    }

    public void setDueDate(LocalDate DueDate) {
        this.DueDate = DueDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", tasktitle='" + getTasktitle() + "'" +
            ", taskdescription='" + getTaskdescription() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", timeStart='" + getTimeStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            ", taskstatus=" + getTaskstatus() +
            ", taskpriority=" + getTaskpriority() +
            ", DueDate='" + getDueDate() + "'" +
            "}";
    }
}
