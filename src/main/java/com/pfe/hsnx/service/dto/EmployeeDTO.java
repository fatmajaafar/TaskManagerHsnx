package com.pfe.hsnx.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Employee} entity.
 */
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull
    private String employeename;

    private String employeephone;

    private String employeefax;

    private String employeeaddress;

    private String employeeemail;

    private LocalDate employeehiredate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getEmployeephone() {
        return employeephone;
    }

    public void setEmployeephone(String employeephone) {
        this.employeephone = employeephone;
    }

    public String getEmployeefax() {
        return employeefax;
    }

    public void setEmployeefax(String employeefax) {
        this.employeefax = employeefax;
    }

    public String getEmployeeaddress() {
        return employeeaddress;
    }

    public void setEmployeeaddress(String employeeaddress) {
        this.employeeaddress = employeeaddress;
    }

    public String getEmployeeemail() {
        return employeeemail;
    }

    public void setEmployeeemail(String employeeemail) {
        this.employeeemail = employeeemail;
    }

    public LocalDate getEmployeehiredate() {
        return employeehiredate;
    }

    public void setEmployeehiredate(LocalDate employeehiredate) {
        this.employeehiredate = employeehiredate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;
        if (employeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + getId() +
            ", employeename='" + getEmployeename() + "'" +
            ", employeephone='" + getEmployeephone() + "'" +
            ", employeefax='" + getEmployeefax() + "'" +
            ", employeeaddress='" + getEmployeeaddress() + "'" +
            ", employeeemail='" + getEmployeeemail() + "'" +
            ", employeehiredate='" + getEmployeehiredate() + "'" +
            "}";
    }
}
