package com.pfe.hsnx.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Department} entity.
 */
public class DepartmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String deptName;

    private String deptNote;


    private Long tblCountryId;

    private String tblCountryCountryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNote() {
        return deptNote;
    }

    public void setDeptNote(String deptNote) {
        this.deptNote = deptNote;
    }

    public Long getTblCountryId() {
        return tblCountryId;
    }

    public void setTblCountryId(Long CountryId) {
        this.tblCountryId = CountryId;
    }

    public String getTblCountryCountryName() {
        return tblCountryCountryName;
    }

    public void setTblCountryCountryName(String CountryCountryName) {
        this.tblCountryCountryName = CountryCountryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DepartmentDTO departmentDTO = (DepartmentDTO) o;
        if (departmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), departmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + getId() +
            ", deptName='" + getDeptName() + "'" +
            ", deptNote='" + getDeptNote() + "'" +
            ", tblCountryId=" + getTblCountryId() +
            ", tblCountryCountryName='" + getTblCountryCountryName() + "'" +
            "}";
    }
}
