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

import com.pfe.hsnx.domain.Task;
import com.pfe.hsnx.domain.*; // for static metamodels
import com.pfe.hsnx.repository.TaskRepository;
import com.pfe.hsnx.repository.search.TaskSearchRepository;
import com.pfe.hsnx.service.dto.TaskCriteria;
import com.pfe.hsnx.service.dto.TaskDTO;
import com.pfe.hsnx.service.mapper.TaskMapper;

/**
 * Service for executing complex queries for {@link Task} entities in the database.
 * The main input is a {@link TaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskDTO} or a {@link Page} of {@link TaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryService extends QueryService<Task> {

    private final Logger log = LoggerFactory.getLogger(TaskQueryService.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskSearchRepository taskSearchRepository;

    public TaskQueryService(TaskRepository taskRepository, TaskMapper taskMapper, TaskSearchRepository taskSearchRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.taskSearchRepository = taskSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findByCriteria(TaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskMapper.toDto(taskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskDTO> findByCriteria(TaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.findAll(specification, page)
            .map(taskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Task> specification = createSpecification(criteria);
        return taskRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Task> createSpecification(TaskCriteria criteria) {
        Specification<Task> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Task_.id));
            }
            if (criteria.getTasktitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTasktitle(), Task_.tasktitle));
            }
            if (criteria.getTaskdescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaskdescription(), Task_.taskdescription));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Task_.dateStart));
            }
            if (criteria.getTimeStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeStart(), Task_.timeStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), Task_.dateEnd));
            }
            if (criteria.getTimeEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeEnd(), Task_.timeEnd));
            }
            if (criteria.getTaskstatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskstatus(), Task_.taskstatus));
            }
            if (criteria.getTaskpriority() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskpriority(), Task_.taskpriority));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), Task_.dueDate));
            }
            if (criteria.getTaskcategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaskcategory(), Task_.taskcategory));
            }
            if (criteria.getTaskstate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskstate(), Task_.taskstate));
            }
            if (criteria.getTblEmployeeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTblEmployeeId(),
                    root -> root.join(Task_.tblEmployee, JoinType.LEFT).get(Employee_.id)));
            }
        }
        return specification;
    }
}
