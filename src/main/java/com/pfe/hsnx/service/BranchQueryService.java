package com.pfe.hsnx.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.pfe.hsnx.domain.Branch;
import com.pfe.hsnx.domain.*; // for static metamodels
import com.pfe.hsnx.repository.BranchRepository;
import com.pfe.hsnx.repository.search.BranchSearchRepository;
import com.pfe.hsnx.service.dto.BranchCriteria;
import com.pfe.hsnx.service.dto.BranchDTO;
import com.pfe.hsnx.service.mapper.BranchMapper;

/**
 * Service for executing complex queries for {@link Branch} entities in the database.
 * The main input is a {@link BranchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchDTO} or a {@link Page} of {@link BranchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchQueryService extends QueryService<Branch> {

    private final Logger log = LoggerFactory.getLogger(BranchQueryService.class);

    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;

    private final BranchSearchRepository branchSearchRepository;

    public BranchQueryService(BranchRepository branchRepository, BranchMapper branchMapper, BranchSearchRepository branchSearchRepository) {
        this.branchRepository = branchRepository;
        this.branchMapper = branchMapper;
        this.branchSearchRepository = branchSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchDTO> findByCriteria(BranchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchMapper.toDto(branchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BranchDTO> findByCriteria(BranchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.findAll(specification, page)
            .map(branchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Branch> specification = createSpecification(criteria);
        return branchRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Branch> createSpecification(BranchCriteria criteria) {
        Specification<Branch> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Branch_.id));
            }
            if (criteria.getBranchName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchName(), Branch_.branchName));
            }
            if (criteria.getBranchPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchPhone(), Branch_.branchPhone));
            }
            if (criteria.getBranchFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchFax(), Branch_.branchFax));
            }
            if (criteria.getBranchAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchAddress(), Branch_.branchAddress));
            }
            if (criteria.getBranchEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchEmail(), Branch_.branchEmail));
            }
            if (criteria.getBranchBanner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBranchBanner(), Branch_.branchBanner));
            }
            if (criteria.getBranchDefaultExercice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBranchDefaultExercice(), Branch_.branchDefaultExercice));
            }
        }
        return specification;
    }
}
