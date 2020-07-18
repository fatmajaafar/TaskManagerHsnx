package com.pfe.hsnx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A Event.
 */
@Entity
@Table(name = "tblEvent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "event")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "EventID")
    private Long id;

    @NotNull
    @Column(name = "EventDescription", nullable = false)
    private String eventdescription;

    @Column(name = "StartTime")
    private Instant starttime;

    @Column(name = "StartDate")
    private LocalDate startdate;

    @Column(name = "EndTime")
    private Instant endtime;

    @Column(name = "EndDate")
    private LocalDate enddate;

    @ManyToOne
    @JsonIgnoreProperties("tblEvents")
    @JoinColumn(name = "TaskID")
    private Task tblTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public Event eventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
        return this;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public Instant getStarttime() {
        return starttime;
    }

    public Event starttime(Instant starttime) {
        this.starttime = starttime;
        return this;
    }

    public void setStarttime(Instant starttime) {
        this.starttime = starttime;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public Event startdate(LocalDate startdate) {
        this.startdate = startdate;
        return this;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public Instant getEndtime() {
        return endtime;
    }

    public Event endtime(Instant endtime) {
        this.endtime = endtime;
        return this;
    }

    public void setEndtime(Instant endtime) {
        this.endtime = endtime;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public Event enddate(LocalDate enddate) {
        this.enddate = enddate;
        return this;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public Task getTblTask() {
        return tblTask;
    }

    public Event tblTask(Task Task) {
        this.tblTask = Task;
        return this;
    }

    public void setTblTask(Task Task) {
        this.tblTask = Task;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventdescription='" + getEventdescription() + "'" +
            ", starttime='" + getStarttime() + "'" +
            ", startdate='" + getStartdate() + "'" +
            ", endtime='" + getEndtime() + "'" +
            ", enddate='" + getEnddate() + "'" +
            "}";
    }
}
