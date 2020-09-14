package com.pfe.hsnx.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Department.
 */
@Entity
@Table(name = "tblDepartment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "DeptID")
    private Long id;

    @NotNull
    @Column(name = "DeptName", nullable = false)
    private String deptName;

    @Column(name = "DeptNote")
    private String deptNote;

    @ManyToOne
    @JsonIgnoreProperties("tblDepartments")
    @JoinColumn(name = "CountryID")
    private Country tblCountry;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public Department deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNote() {
        return deptNote;
    }

    public Department deptNote(String deptNote) {
        this.deptNote = deptNote;
        return this;
    }

    public void setDeptNote(String deptNote) {
        this.deptNote = deptNote;
    }

    public Country getTblCountry() {
        return tblCountry;
    }

    public Department tblCountry(Country Country) {
        this.tblCountry = Country;
        return this;
    }

    public void setTblCountry(Country Country) {
        this.tblCountry = Country;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", deptName='" + getDeptName() + "'" +
            ", deptNote='" + getDeptNote() + "'" +
            "}";
    }
}
