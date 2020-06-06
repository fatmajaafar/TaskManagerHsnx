package com.pfe.hsnx.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String tasktitle;

    private String taskdescription;

    private LocalDate dateStart;

    private Instant timeStart;

    private LocalDate dateEnd;

    private Instant timeEnd;

    private Integer taskstatus;

    private Integer taskpriority;

    private LocalDate DueDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTasktitle() {
        return tasktitle;
    }

    public void setTasktitle(String tasktitle) {
        this.tasktitle = tasktitle;
    }

    public String getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Instant getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(Integer taskstatus) {
        this.taskstatus = taskstatus;
    }

    public Integer getTaskpriority() {
        return taskpriority;
    }

    public void setTaskpriority(Integer taskpriority) {
        this.taskpriority = taskpriority;
    }

    public LocalDate getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDate DueDate) {
        this.DueDate = DueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
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
