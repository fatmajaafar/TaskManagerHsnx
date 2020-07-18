package com.pfe.hsnx.service.mapper;


import com.pfe.hsnx.domain.*;
import com.pfe.hsnx.service.dto.JobDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobMapper extends EntityMapper<JobDTO, Job> {



    default Job fromId(Long id) {
        if (id == null) {
            return null;
        }
        Job job = new Job();
        job.setId(id);
        return job;
    }
}
