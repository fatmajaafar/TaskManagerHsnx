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
 * Criteria class for the {@link com.pfe.hsnx.domain.Event} entity. This class is used
 * in {@link com.pfe.hsnx.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter Description;

    private InstantFilter starttime;

    private LocalDateFilter startdate;

    private InstantFilter endtime;

    private LocalDateFilter enddate;

    public EventCriteria() {
    }

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.Description = other.Description == null ? null : other.Description.copy();
        this.starttime = other.starttime == null ? null : other.starttime.copy();
        this.startdate = other.startdate == null ? null : other.startdate.copy();
        this.endtime = other.endtime == null ? null : other.endtime.copy();
        this.enddate = other.enddate == null ? null : other.enddate.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescription() {
        return Description;
    }

    public void setDescription(StringFilter Description) {
        this.Description = Description;
    }

    public InstantFilter getStarttime() {
        return starttime;
    }

    public void setStarttime(InstantFilter starttime) {
        this.starttime = starttime;
    }

    public LocalDateFilter getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateFilter startdate) {
        this.startdate = startdate;
    }

    public InstantFilter getEndtime() {
        return endtime;
    }

    public void setEndtime(InstantFilter endtime) {
        this.endtime = endtime;
    }

    public LocalDateFilter getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDateFilter enddate) {
        this.enddate = enddate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(Description, that.Description) &&
            Objects.equals(starttime, that.starttime) &&
            Objects.equals(startdate, that.startdate) &&
            Objects.equals(endtime, that.endtime) &&
            Objects.equals(enddate, that.enddate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        Description,
        starttime,
        startdate,
        endtime,
        enddate
        );
    }

    @Override
    public String toString() {
        return "EventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (Description != null ? "Description=" + Description + ", " : "") +
                (starttime != null ? "starttime=" + starttime + ", " : "") +
                (startdate != null ? "startdate=" + startdate + ", " : "") +
                (endtime != null ? "endtime=" + endtime + ", " : "") +
                (enddate != null ? "enddate=" + enddate + ", " : "") +
            "}";
    }

}
