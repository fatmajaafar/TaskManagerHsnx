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
    @Column(name = "EmpID")
    private Long id;

    @NotNull
    @Column(name = "EmployeeName", nullable = false)
    private String employeename;

    @Column(name = "EmployeePhone")
    private String employeephone;

    @Column(name = "EmployeeFax")
    private String employeefax;

    @Column(name = "EmployeeAdresse")
    private String employeeaddress;

    @Column(name = "EmployeeEmail")
    private String employeeemail;

    @Column(name = "EmployeeHiredate")
    private LocalDate employeehiredate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeename() {
        return employeename;
    }

    public Employee employeename(String employeename) {
        this.employeename = employeename;
        return this;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getEmployeephone() {
        return employeephone;
    }

    public Employee employeephone(String employeephone) {
        this.employeephone = employeephone;
        return this;
    }

    public void setEmployeephone(String employeephone) {
        this.employeephone = employeephone;
    }

    public String getEmployeefax() {
        return employeefax;
    }

    public Employee employeefax(String employeefax) {
        this.employeefax = employeefax;
        return this;
    }

    public void setEmployeefax(String employeefax) {
        this.employeefax = employeefax;
    }

    public String getEmployeeaddress() {
        return employeeaddress;
    }

    public Employee employeeaddress(String employeeaddress) {
        this.employeeaddress = employeeaddress;
        return this;
    }

    public void setEmployeeaddress(String employeeaddress) {
        this.employeeaddress = employeeaddress;
    }

    public String getEmployeeemail() {
        return employeeemail;
    }

    public Employee employeeemail(String employeeemail) {
        this.employeeemail = employeeemail;
        return this;
    }

    public void setEmployeeemail(String employeeemail) {
        this.employeeemail = employeeemail;
    }

    public LocalDate getEmployeehiredate() {
        return employeehiredate;
    }

    public Employee employeehiredate(LocalDate employeehiredate) {
        this.employeehiredate = employeehiredate;
        return this;
    }

    public void setEmployeehiredate(LocalDate employeehiredate) {
        this.employeehiredate = employeehiredate;
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
            ", employeename='" + getEmployeename() + "'" +
            ", employeephone='" + getEmployeephone() + "'" +
            ", employeefax='" + getEmployeefax() + "'" +
            ", employeeaddress='" + getEmployeeaddress() + "'" +
            ", employeeemail='" + getEmployeeemail() + "'" +
            ", employeehiredate='" + getEmployeehiredate() + "'" +
            "}";
    }
}
