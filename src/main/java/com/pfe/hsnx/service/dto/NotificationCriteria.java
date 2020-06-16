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

/**
 * Criteria class for the {@link com.pfe.hsnx.domain.Notification} entity. This class is used
 * in {@link com.pfe.hsnx.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private BooleanFilter handled;

    private StringFilter notifFrom;

    private StringFilter notifto;

    public NotificationCriteria() {
    }

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.handled = other.handled == null ? null : other.handled.copy();
        this.notifFrom = other.notifFrom == null ? null : other.notifFrom.copy();
        this.notifto = other.notifto == null ? null : other.notifto.copy();
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public BooleanFilter getHandled() {
        return handled;
    }

    public void setHandled(BooleanFilter handled) {
        this.handled = handled;
    }

    public StringFilter getNotifFrom() {
        return notifFrom;
    }

    public void setNotifFrom(StringFilter notifFrom) {
        this.notifFrom = notifFrom;
    }

    public StringFilter getNotifto() {
        return notifto;
    }

    public void setNotifto(StringFilter notifto) {
        this.notifto = notifto;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NotificationCriteria that = (NotificationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(handled, that.handled) &&
            Objects.equals(notifFrom, that.notifFrom) &&
            Objects.equals(notifto, that.notifto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        message,
        handled,
        notifFrom,
        notifto
        );
    }

    @Override
    public String toString() {
        return "NotificationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (handled != null ? "handled=" + handled + ", " : "") +
                (notifFrom != null ? "notifFrom=" + notifFrom + ", " : "") +
                (notifto != null ? "notifto=" + notifto + ", " : "") +
            "}";
    }

}
