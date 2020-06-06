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

    private StringFilter branchName;

    private StringFilter branchPhone;

    private StringFilter branchFax;

    private StringFilter branchAddress;

    private StringFilter branchEmail;

    private LocalDateFilter branchHiredate;

    public EmployeeCriteria() {
    }

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.branchName = other.branchName == null ? null : other.branchName.copy();
        this.branchPhone = other.branchPhone == null ? null : other.branchPhone.copy();
        this.branchFax = other.branchFax == null ? null : other.branchFax.copy();
        this.branchAddress = other.branchAddress == null ? null : other.branchAddress.copy();
        this.branchEmail = other.branchEmail == null ? null : other.branchEmail.copy();
        this.branchHiredate = other.branchHiredate == null ? null : other.branchHiredate.copy();
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

    public StringFilter getBranchName() {
        return branchName;
    }

    public void setBranchName(StringFilter branchName) {
        this.branchName = branchName;
    }

    public StringFilter getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(StringFilter branchPhone) {
        this.branchPhone = branchPhone;
    }

    public StringFilter getBranchFax() {
        return branchFax;
    }

    public void setBranchFax(StringFilter branchFax) {
        this.branchFax = branchFax;
    }

    public StringFilter getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(StringFilter branchAddress) {
        this.branchAddress = branchAddress;
    }

    public StringFilter getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(StringFilter branchEmail) {
        this.branchEmail = branchEmail;
    }

    public LocalDateFilter getBranchHiredate() {
        return branchHiredate;
    }

    public void setBranchHiredate(LocalDateFilter branchHiredate) {
        this.branchHiredate = branchHiredate;
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
            Objects.equals(branchName, that.branchName) &&
            Objects.equals(branchPhone, that.branchPhone) &&
            Objects.equals(branchFax, that.branchFax) &&
            Objects.equals(branchAddress, that.branchAddress) &&
            Objects.equals(branchEmail, that.branchEmail) &&
            Objects.equals(branchHiredate, that.branchHiredate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        branchName,
        branchPhone,
        branchFax,
        branchAddress,
        branchEmail,
        branchHiredate
        );
    }

    @Override
    public String toString() {
        return "EmployeeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (branchName != null ? "branchName=" + branchName + ", " : "") +
                (branchPhone != null ? "branchPhone=" + branchPhone + ", " : "") +
                (branchFax != null ? "branchFax=" + branchFax + ", " : "") +
                (branchAddress != null ? "branchAddress=" + branchAddress + ", " : "") +
                (branchEmail != null ? "branchEmail=" + branchEmail + ", " : "") +
                (branchHiredate != null ? "branchHiredate=" + branchHiredate + ", " : "") +
            "}";
    }

}
