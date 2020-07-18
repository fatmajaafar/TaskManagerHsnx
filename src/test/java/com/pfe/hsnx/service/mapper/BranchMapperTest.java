package com.pfe.hsnx.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BranchMapperTest {

    private BranchMapper branchMapper;

    @BeforeEach
    public void setUp() {
        branchMapper = new BranchMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(branchMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(branchMapper.fromId(null)).isNull();
    }
}
