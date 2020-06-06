package com.pfe.hsnx.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Employee.
 */
@Entity
@Table(name = "TblEmployee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "Employee")
    private Long id;

    @NotNull
    @Column(name = "EmployeeName", nullable = false)
    private String branchName;

    @Column(name = "EmployeePhone")
    private String branchPhone;

    @Column(name = "EmployeeFax")
    private String branchFax;

    @Column(name = "EmployeeAdresse")
    private String branchAddress;

    @Column(name = "EmployeeEmail")
    private String branchEmail;

    @Column(name = "EmployeeHiredate")
    private LocalDate branchHiredate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public Employee branchName(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public Employee branchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
        return this;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchFax() {
        return branchFax;
    }

    public Employee branchFax(String branchFax) {
        this.branchFax = branchFax;
        return this;
    }

    public void setBranchFax(String branchFax) {
        this.branchFax = branchFax;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public Employee branchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
        return this;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public Employee branchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
        return this;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public LocalDate getBranchHiredate() {
        return branchHiredate;
    }

    public Employee branchHiredate(LocalDate branchHiredate) {
        this.branchHiredate = branchHiredate;
        return this;
    }

    public void setBranchHiredate(LocalDate branchHiredate) {
        this.branchHiredate = branchHiredate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", branchName='" + getBranchName() + "'" +
            ", branchPhone='" + getBranchPhone() + "'" +
            ", branchFax='" + getBranchFax() + "'" +
            ", branchAddress='" + getBranchAddress() + "'" +
            ", branchEmail='" + getBranchEmail() + "'" +
            ", branchHiredate='" + getBranchHiredate() + "'" +
            "}";
    }
}
