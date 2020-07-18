package com.pfe.hsnx.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Event} entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String eventdescription;

    private Instant starttime;

    private LocalDate startdate;

    private Instant endtime;

    private LocalDate enddate;


    private Long tblTaskId;

    private String tblTaskTasktitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public Instant getStarttime() {
        return starttime;
    }

    public void setStarttime(Instant starttime) {
        this.starttime = starttime;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public Instant getEndtime() {
        return endtime;
    }

    public void setEndtime(Instant endtime) {
        this.endtime = endtime;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public Long getTblTaskId() {
        return tblTaskId;
    }

    public void setTblTaskId(Long TaskId) {
        this.tblTaskId = TaskId;
    }

    public String getTblTaskTasktitle() {
        return tblTaskTasktitle;
    }

    public void setTblTaskTasktitle(String TaskTasktitle) {
        this.tblTaskTasktitle = TaskTasktitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (eventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + getId() +
            ", eventdescription='" + getEventdescription() + "'" +
            ", starttime='" + getStarttime() + "'" +
            ", startdate='" + getStartdate() + "'" +
            ", endtime='" + getEndtime() + "'" +
            ", enddate='" + getEnddate() + "'" +
            ", tblTaskId=" + getTblTaskId() +
            ", tblTaskTasktitle='" + getTblTaskTasktitle() + "'" +
            "}";
    }
}
