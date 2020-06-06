package com.pfe.hsnx.service.mapper;


import com.pfe.hsnx.domain.*;
import com.pfe.hsnx.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EventMapper extends EntityMapper<EventDTO, Event> {



    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
