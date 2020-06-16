package com.pfe.hsnx.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Notification.
 */
@Entity
@Table(name = "TblNotification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "NotificationID")
    private Long id;

    @NotNull
    @Column(name = "Message", nullable = false)
    private String message;

    @Column(name = "Handled")
    private Boolean handled;

    @Column(name = "NotifFrom")
    private String notifFrom;

    @Column(name = "NotifTo")
    private String notifto;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Notification message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isHandled() {
        return handled;
    }

    public Notification handled(Boolean handled) {
        this.handled = handled;
        return this;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public String getNotifFrom() {
        return notifFrom;
    }

    public Notification notifFrom(String notifFrom) {
        this.notifFrom = notifFrom;
        return this;
    }

    public void setNotifFrom(String notifFrom) {
        this.notifFrom = notifFrom;
    }

    public String getNotifto() {
        return notifto;
    }

    public Notification notifto(String notifto) {
        this.notifto = notifto;
        return this;
    }

    public void setNotifto(String notifto) {
        this.notifto = notifto;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", handled='" + isHandled() + "'" +
            ", notifFrom='" + getNotifFrom() + "'" +
            ", notifto='" + getNotifto() + "'" +
            "}";
    }
}
