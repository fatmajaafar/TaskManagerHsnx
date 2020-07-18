package com.pfe.hsnx.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.pfe.hsnx.domain.Task} entity. This class is used
 * in {@link com.pfe.hsnx.web.rest.TaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tasktitle;

    private StringFilter taskdescription;

    private LocalDateFilter dateStart;

    private InstantFilter timeStart;

    private LocalDateFilter dateEnd;

    private InstantFilter timeEnd;

    private IntegerFilter taskstatus;

    private IntegerFilter taskpriority;

    private LocalDateFilter dueDate;

    private StringFilter taskcategory;

    private IntegerFilter taskstate;

    private LongFilter tblEmployeeId;

    public TaskCriteria() {
    }

    public TaskCriteria(TaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tasktitle = other.tasktitle == null ? null : other.tasktitle.copy();
        this.taskdescription = other.taskdescription == null ? null : other.taskdescription.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.timeStart = other.timeStart == null ? null : other.timeStart.copy();
        this.dateEnd = other.dateEnd == null ? null : other.dateEnd.copy();
        this.timeEnd = other.timeEnd == null ? null : other.timeEnd.copy();
        this.taskstatus = other.taskstatus == null ? null : other.taskstatus.copy();
        this.taskpriority = other.taskpriority == null ? null : other.taskpriority.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.taskcategory = other.taskcategory == null ? null : other.taskcategory.copy();
        this.taskstate = other.taskstate == null ? null : other.taskstate.copy();
        this.tblEmployeeId = other.tblEmployeeId == null ? null : other.tblEmployeeId.copy();
    }

    @Override
    public TaskCriteria copy() {
        return new TaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTasktitle() {
        return tasktitle;
    }

    public void setTasktitle(StringFilter tasktitle) {
        this.tasktitle = tasktitle;
    }

    public StringFilter getTaskdescription() {
        return taskdescription;
    }

    public void setTaskdescription(StringFilter taskdescription) {
        this.taskdescription = taskdescription;
    }

    public LocalDateFilter getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateFilter dateStart) {
        this.dateStart = dateStart;
    }

    public InstantFilter getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(InstantFilter timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateFilter getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateFilter dateEnd) {
        this.dateEnd = dateEnd;
    }

    public InstantFilter getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(InstantFilter timeEnd) {
        this.timeEnd = timeEnd;
    }

    public IntegerFilter getTaskstatus() {
        return taskstatus;
    }

    public void setTaskstatus(IntegerFilter taskstatus) {
        this.taskstatus = taskstatus;
    }

    public IntegerFilter getTaskpriority() {
        return taskpriority;
    }

    public void setTaskpriority(IntegerFilter taskpriority) {
        this.taskpriority = taskpriority;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public StringFilter getTaskcategory() {
        return taskcategory;
    }

    public void setTaskcategory(StringFilter taskcategory) {
        this.taskcategory = taskcategory;
    }

    public IntegerFilter getTaskstate() {
        return taskstate;
    }

    public void setTaskstate(IntegerFilter taskstate) {
        this.taskstate = taskstate;
    }

    public LongFilter getTblEmployeeId() {
        return tblEmployeeId;
    }

    public void setTblEmployeeId(LongFilter tblEmployeeId) {
        this.tblEmployeeId = tblEmployeeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskCriteria that = (TaskCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tasktitle, that.tasktitle) &&
            Objects.equals(taskdescription, that.taskdescription) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(timeStart, that.timeStart) &&
            Objects.equals(dateEnd, that.dateEnd) &&
            Objects.equals(timeEnd, that.timeEnd) &&
            Objects.equals(taskstatus, that.taskstatus) &&
            Objects.equals(taskpriority, that.taskpriority) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(taskcategory, that.taskcategory) &&
            Objects.equals(taskstate, that.taskstate) &&
            Objects.equals(tblEmployeeId, that.tblEmployeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tasktitle,
        taskdescription,
        dateStart,
        timeStart,
        dateEnd,
        timeEnd,
        taskstatus,
        taskpriority,
        dueDate,
        taskcategory,
        taskstate,
        tblEmployeeId
        );
    }

    @Override
    public String toString() {
        return "TaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tasktitle != null ? "tasktitle=" + tasktitle + ", " : "") +
                (taskdescription != null ? "taskdescription=" + taskdescription + ", " : "") +
                (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
                (timeStart != null ? "timeStart=" + timeStart + ", " : "") +
                (dateEnd != null ? "dateEnd=" + dateEnd + ", " : "") +
                (timeEnd != null ? "timeEnd=" + timeEnd + ", " : "") +
                (taskstatus != null ? "taskstatus=" + taskstatus + ", " : "") +
                (taskpriority != null ? "taskpriority=" + taskpriority + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (taskcategory != null ? "taskcategory=" + taskcategory + ", " : "") +
                (taskstate != null ? "taskstate=" + taskstate + ", " : "") +
                (tblEmployeeId != null ? "tblEmployeeId=" + tblEmployeeId + ", " : "") +
            "}";
    }

}
