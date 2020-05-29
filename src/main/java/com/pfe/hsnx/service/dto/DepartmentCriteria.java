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

/**
 * Criteria class for the {@link com.pfe.hsnx.domain.Department} entity. This class is used
 * in {@link com.pfe.hsnx.web.rest.DepartmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /departments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DepartmentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deptName;

    private StringFilter deptNote;

    public DepartmentCriteria() {
    }

    public DepartmentCriteria(DepartmentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.deptName = other.deptName == null ? null : other.deptName.copy();
        this.deptNote = other.deptNote == null ? null : other.deptNote.copy();
    }

    @Override
    public DepartmentCriteria copy() {
        return new DepartmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDeptName() {
        return deptName;
    }

    public void setDeptName(StringFilter deptName) {
        this.deptName = deptName;
    }

    public StringFilter getDeptNote() {
        return deptNote;
    }

    public void setDeptNote(StringFilter deptNote) {
        this.deptNote = deptNote;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DepartmentCriteria that = (DepartmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(deptName, that.deptName) &&
            Objects.equals(deptNote, that.deptNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        deptName,
        deptNote
        );
    }

    @Override
    public String toString() {
        return "DepartmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (deptName != null ? "deptName=" + deptName + ", " : "") +
                (deptNote != null ? "deptNote=" + deptNote + ", " : "") +
            "}";
    }

}
