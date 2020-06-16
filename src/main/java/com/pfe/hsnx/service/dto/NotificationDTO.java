package com.pfe.hsnx.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String message;

    private Boolean handled;

    private String notifFrom;

    private String notifto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isHandled() {
        return handled;
    }

    public void setHandled(Boolean handled) {
        this.handled = handled;
    }

    public String getNotifFrom() {
        return notifFrom;
    }

    public void setNotifFrom(String notifFrom) {
        this.notifFrom = notifFrom;
    }

    public String getNotifto() {
        return notifto;
    }

    public void setNotifto(String notifto) {
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

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", handled='" + isHandled() + "'" +
            ", notifFrom='" + getNotifFrom() + "'" +
            ", notifto='" + getNotifto() + "'" +
            "}";
    }
}
