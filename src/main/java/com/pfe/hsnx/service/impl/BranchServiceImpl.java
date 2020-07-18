package com.pfe.hsnx.service.impl;

import com.pfe.hsnx.service.BranchService;
import com.pfe.hsnx.domain.Branch;
import com.pfe.hsnx.repository.BranchRepository;
import com.pfe.hsnx.repository.search.BranchSearchRepository;
import com.pfe.hsnx.service.dto.BranchDTO;
import com.pfe.hsnx.service.mapper.BranchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Branch}.
 */
@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    private final Logger log = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final BranchSearchRepository branchSearchRepository;

    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper, BranchSearchRepository branchSearchRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.branchSearchRepository = branchSearchRepository;
    }

    /**
     * Save a branch.
     *
     * @param branchDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BranchDTO save(BranchDTO branchDTO) {
        log.debug("Request to save Branch : {}", branchDTO);
        Branch branch = branchMapper.toEntity(branchDTO);
        branch = branchRepository.save(branch);
        BranchDTO result = branchMapper.toDto(branch);
        branchSearchRepository.save(branch);
        return result;
    }

    /**
     * Get all the branches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BranchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Branches");
        return branchRepository.findAll(pageable)
            .map(branchMapper::toDto);
    }

    /**
     * Get one branch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BranchDTO> findOne(Long id) {
        log.debug("Request to get Branch : {}", id);
        return branchRepository.findById(id)
            .map(branchMapper::toDto);
    }

    /**
     * Delete the branch by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Branch : {}", id);
        branchRepository.deleteById(id);
        branchSearchRepository.deleteById(id);
    }

    /**
     * Search for the branch corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BranchDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Branches for query {}", query);
        return branchSearchRepository.search(queryStringQuery(query), pageable)
            .map(branchMapper::toDto);
    }
}
