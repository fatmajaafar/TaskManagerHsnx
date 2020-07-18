package com.pfe.hsnx.service.mapper;


import com.pfe.hsnx.domain.*;
import com.pfe.hsnx.service.dto.BranchDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Branch} and its DTO {@link BranchDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BranchMapper extends EntityMapper<BranchDTO, Branch> {



    default Branch fromId(Long id) {
        if (id == null) {
            return null;
        }
        Branch branch = new Branch();
        branch.setId(id);
        return branch;
    }
}
