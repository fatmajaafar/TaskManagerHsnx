package com.pfe.hsnx.service.mapper;


import com.pfe.hsnx.domain.*;
import com.pfe.hsnx.service.dto.TaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

    @Mapping(source = "tblEmployee.id", target = "tblEmployeeId")
    @Mapping(source = "tblEmployee.employeename", target = "tblEmployeeEmployeename")
    TaskDTO toDto(Task task);

    @Mapping(source = "tblEmployeeId", target = "tblEmployee")
    Task toEntity(TaskDTO taskDTO);

    default Task fromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
