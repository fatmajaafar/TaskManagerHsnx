package com.pfe.hsnx.service.mapper;


import com.pfe.hsnx.domain.*;
import com.pfe.hsnx.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {JobMapper.class, DepartmentMapper.class, BranchMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {

    @Mapping(source = "tblJob.id", target = "tblJobId")
    @Mapping(source = "tblJob.jobName", target = "tblJobJobName")
    @Mapping(source = "tblDepartment.id", target = "tblDepartmentId")
    @Mapping(source = "tblDepartment.deptName", target = "tblDepartmentDeptName")
    @Mapping(source = "tblBranch.id", target = "tblBranchId")
    @Mapping(source = "tblBranch.branchName", target = "tblBranchBranchName")
    EmployeeDTO toDto(Employee employee);

    @Mapping(source = "tblJobId", target = "tblJob")
    @Mapping(source = "tblDepartmentId", target = "tblDepartment")
    @Mapping(source = "tblBranchId", target = "tblBranch")
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
