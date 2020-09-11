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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.pfe.hsnx.domain.Employee} entity. This class is used
 * in {@link com.pfe.hsnx.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmployeeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeename;

    private StringFilter employeelastname;

    private StringFilter employeephone;

    private StringFilter employeefax;

    private StringFilter employeeaddress;

    private StringFilter employeeemail;

    private LocalDateFilter employeehiredate;

    private LongFilter tblJobId;

    private LongFilter tblDepartmentId;

    private LongFilter tblBranchId;

    public EmployeeCriteria() {
    }

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeename = other.employeename == null ? null : other.employeename.copy();
        this.employeelastname = other.employeelastname == null ? null : other.employeelastname.copy();
        this.employeephone = other.employeephone == null ? null : other.employeephone.copy();
        this.employeefax = other.employeefax == null ? null : other.employeefax.copy();
        this.employeeaddress = other.employeeaddress == null ? null : other.employeeaddress.copy();
        this.employeeemail = other.employeeemail == null ? null : other.employeeemail.copy();
        this.employeehiredate = other.employeehiredate == null ? null : other.employeehiredate.copy();
        this.tblJobId = other.tblJobId == null ? null : other.tblJobId.copy();
        this.tblDepartmentId = other.tblDepartmentId == null ? null : other.tblDepartmentId.copy();
        this.tblBranchId = other.tblBranchId == null ? null : other.tblBranchId.copy();
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEmployeename() {
        return employeename;
    }

    public void setEmployeename(StringFilter employeename) {
        this.employeename = employeename;
    }

    public StringFilter getEmployeelastname() {
        return employeelastname;
    }

    public void setEmployeelastname(StringFilter employeelastname) {
        this.employeelastname = employeelastname;
    }

    public StringFilter getEmployeephone() {
        return employeephone;
    }

    public void setEmployeephone(StringFilter employeephone) {
        this.employeephone = employeephone;
    }

    public StringFilter getEmployeefax() {
        return employeefax;
    }

    public void setEmployeefax(StringFilter employeefax) {
        this.employeefax = employeefax;
    }

    public StringFilter getEmployeeaddress() {
        return employeeaddress;
    }

    public void setEmployeeaddress(StringFilter employeeaddress) {
        this.employeeaddress = employeeaddress;
    }

    public StringFilter getEmployeeemail() {
        return employeeemail;
    }

    public void setEmployeeemail(StringFilter employeeemail) {
        this.employeeemail = employeeemail;
    }

    public LocalDateFilter getEmployeehiredate() {
        return employeehiredate;
    }

    public void setEmployeehiredate(LocalDateFilter employeehiredate) {
        this.employeehiredate = employeehiredate;
    }

    public LongFilter getTblJobId() {
        return tblJobId;
    }

    public void setTblJobId(LongFilter tblJobId) {
        this.tblJobId = tblJobId;
    }

    public LongFilter getTblDepartmentId() {
        return tblDepartmentId;
    }

    public void setTblDepartmentId(LongFilter tblDepartmentId) {
        this.tblDepartmentId = tblDepartmentId;
    }

    public LongFilter getTblBranchId() {
        return tblBranchId;
    }

    public void setTblBranchId(LongFilter tblBranchId) {
        this.tblBranchId = tblBranchId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(employeename, that.employeename) &&
            Objects.equals(employeelastname, that.employeelastname) &&
            Objects.equals(employeephone, that.employeephone) &&
            Objects.equals(employeefax, that.employeefax) &&
            Objects.equals(employeeaddress, that.employeeaddress) &&
            Objects.equals(employeeemail, that.employeeemail) &&
            Objects.equals(employeehiredate, that.employeehiredate) &&
            Objects.equals(tblJobId, that.tblJobId) &&
            Objects.equals(tblDepartmentId, that.tblDepartmentId) &&
            Objects.equals(tblBranchId, that.tblBranchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        employeename,
        employeelastname,
        employeephone,
        employeefax,
        employeeaddress,
        employeeemail,
        employeehiredate,
        tblJobId,
        tblDepartmentId,
        tblBranchId
        );
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (employeename != null ? "employeename=" + employeename + ", " : "") +
                (employeelastname != null ? "employeelastname=" + employeelastname + ", " : "") +
                (employeephone != null ? "employeephone=" + employeephone + ", " : "") +
                (employeefax != null ? "employeefax=" + employeefax + ", " : "") +
                (employeeaddress != null ? "employeeaddress=" + employeeaddress + ", " : "") +
                (employeeemail != null ? "employeeemail=" + employeeemail + ", " : "") +
                (employeehiredate != null ? "employeehiredate=" + employeehiredate + ", " : "") +
                (tblJobId != null ? "tblJobId=" + tblJobId + ", " : "") +
                (tblDepartmentId != null ? "tblDepartmentId=" + tblDepartmentId + ", " : "") +
                (tblBranchId != null ? "tblBranchId=" + tblBranchId + ", " : "") +
            "}";
    }

}
