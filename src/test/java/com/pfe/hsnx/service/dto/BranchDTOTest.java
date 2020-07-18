package com.pfe.hsnx.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.pfe.hsnx.web.rest.TestUtil;

public class BranchDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchDTO.class);
        BranchDTO branchDTO1 = new BranchDTO();
        branchDTO1.setId(1L);
        BranchDTO branchDTO2 = new BranchDTO();
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
        branchDTO2.setId(branchDTO1.getId());
        assertThat(branchDTO1).isEqualTo(branchDTO2);
        branchDTO2.setId(2L);
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
        branchDTO1.setId(null);
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
    }
}
