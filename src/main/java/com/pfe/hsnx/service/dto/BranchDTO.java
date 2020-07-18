package com.pfe.hsnx.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pfe.hsnx.domain.Branch} entity.
 */
public class BranchDTO implements Serializable {

    private Long id;

    @NotNull
    private String branchName;

    private String branchPhone;

    private String branchFax;

    private String branchAddress;

    private String branchEmail;

    private String branchBanner;

    private Integer branchDefaultExercice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchFax() {
        return branchFax;
    }

    public void setBranchFax(String branchFax) {
        this.branchFax = branchFax;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchEmail() {
        return branchEmail;
    }

    public void setBranchEmail(String branchEmail) {
        this.branchEmail = branchEmail;
    }

    public String getBranchBanner() {
        return branchBanner;
    }

    public void setBranchBanner(String branchBanner) {
        this.branchBanner = branchBanner;
    }

    public Integer getBranchDefaultExercice() {
        return branchDefaultExercice;
    }

    public void setBranchDefaultExercice(Integer branchDefaultExercice) {
        this.branchDefaultExercice = branchDefaultExercice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BranchDTO branchDTO = (BranchDTO) o;
        if (branchDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), branchDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BranchDTO{" +
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
