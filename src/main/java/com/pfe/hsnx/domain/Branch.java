package com.pfe.hsnx.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Branch.
 */
@Entity
@Table(name = "tblBranch")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "branch")
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "BranchID")
    private Long id;

    @NotNull
    @Column(name = "BranchName", nullable = false)
    private String branchName;

    @Column(name = "BranchPhone")
    private String branchPhone;

    @Column(name = "BranchFax")
    private String branchFax;

    @Column(name = "BranchAddress")
    private String branchAddress;

    @Column(name = "BranchEmail")
    private String branchEmail;

    @Column(name = "BranchBanner")
    private String branchBanner;

    @Column(name = "BranchDefaultExercice")
    private Integer branchDefaultExercice;

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

    public Branch branchName(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public Branch branchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
        return this;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchFax() {
        return branchFax;
    }

    public Branch branchFax(String branchFax) {
        this.branchFax = branchFax;
        return this;
    }

    public void setBranchFax(String branchFax) {
        this.branchFax = branchFax;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public Branch branchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
        return this;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public Branch branchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
        return this;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public String getBranchBanner() {
        return branchBanner;
    }

    public Branch branchBanner(String branchBanner) {
        this.branchBanner = branchBanner;
        return this;
    }

    public void setBranchBanner(String branchBanner) {
        this.branchBanner = branchBanner;
    }

    public Integer getBranchDefaultExercice() {
        return branchDefaultExercice;
    }

    public Branch branchDefaultExercice(Integer branchDefaultExercice) {
        this.branchDefaultExercice = branchDefaultExercice;
        return this;
    }

    public void setBranchDefaultExercice(Integer branchDefaultExercice) {
        this.branchDefaultExercice = branchDefaultExercice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }
        return id != null && id.equals(((Branch) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Branch{" +
            "id=" + getId() +
            ", branchName='" + getBranchName() + "'" +
            ", branchPhone='" + getBranchPhone() + "'" +
            ", branchFax='" + getBranchFax() + "'" +
            ", branchAddress='" + getBranchAddress() + "'" +
            ", branchEmail='" + getBranchEmail() + "'" +
            ", branchBanner='" + getBranchBanner() + "'" +
            ", branchDefaultExercice=" + getBranchDefaultExercice() +
            "}";
    }
}
