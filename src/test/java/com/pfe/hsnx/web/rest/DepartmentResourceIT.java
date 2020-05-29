package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Department;
import com.pfe.hsnx.repository.DepartmentRepository;
import com.pfe.hsnx.repository.search.DepartmentSearchRepository;
import com.pfe.hsnx.service.DepartmentService;
import com.pfe.hsnx.service.dto.DepartmentDTO;
import com.pfe.hsnx.service.mapper.DepartmentMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.DepartmentCriteria;
import com.pfe.hsnx.service.DepartmentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.pfe.hsnx.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DepartmentResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class DepartmentResourceIT {

    private static final String DEFAULT_DEPT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_DEPT_NOTE = "BBBBBBBBBB";

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.DepartmentSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepartmentSearchRepository mockDepartmentSearchRepository;

    @Autowired
    private DepartmentQueryService departmentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDepartmentMockMvc;

    private Department department;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepartmentResource departmentResource = new DepartmentResource(departmentService, departmentQueryService);
        this.restDepartmentMockMvc = MockMvcBuilders.standaloneSetup(departmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createEntity(EntityManager em) {
        Department department = new Department()
            .deptName(DEFAULT_DEPT_NAME)
            .deptNote(DEFAULT_DEPT_NOTE);
        return department;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Department createUpdatedEntity(EntityManager em) {
        Department department = new Department()
            .deptName(UPDATED_DEPT_NAME)
            .deptNote(UPDATED_DEPT_NOTE);
        return department;
    }

    @BeforeEach
    public void initTest() {
        department = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepartment() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);
        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isCreated());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate + 1);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getDeptName()).isEqualTo(DEFAULT_DEPT_NAME);
        assertThat(testDepartment.getDeptNote()).isEqualTo(DEFAULT_DEPT_NOTE);

        // Validate the Department in Elasticsearch
        verify(mockDepartmentSearchRepository, times(1)).save(testDepartment);
    }

    @Test
    @Transactional
    public void createDepartmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = departmentRepository.findAll().size();

        // Create the Department with an existing ID
        department.setId(1L);
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Department in Elasticsearch
        verify(mockDepartmentSearchRepository, times(0)).save(department);
    }


    @Test
    @Transactional
    public void checkDeptNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = departmentRepository.findAll().size();
        // set the field null
        department.setDeptName(null);

        // Create the Department, which fails.
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        restDepartmentMockMvc.perform(post("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepartments() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].deptNote").value(hasItem(DEFAULT_DEPT_NOTE)));
    }
    
    @Test
    @Transactional
    public void getDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(department.getId().intValue()))
            .andExpect(jsonPath("$.deptName").value(DEFAULT_DEPT_NAME))
            .andExpect(jsonPath("$.deptNote").value(DEFAULT_DEPT_NOTE));
    }


    @Test
    @Transactional
    public void getDepartmentsByIdFiltering() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        Long id = department.getId();

        defaultDepartmentShouldBeFound("id.equals=" + id);
        defaultDepartmentShouldNotBeFound("id.notEquals=" + id);

        defaultDepartmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartmentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepartmentsByDeptNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName equals to DEFAULT_DEPT_NAME
        defaultDepartmentShouldBeFound("deptName.equals=" + DEFAULT_DEPT_NAME);

        // Get all the departmentList where deptName equals to UPDATED_DEPT_NAME
        defaultDepartmentShouldNotBeFound("deptName.equals=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName not equals to DEFAULT_DEPT_NAME
        defaultDepartmentShouldNotBeFound("deptName.notEquals=" + DEFAULT_DEPT_NAME);

        // Get all the departmentList where deptName not equals to UPDATED_DEPT_NAME
        defaultDepartmentShouldBeFound("deptName.notEquals=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNameIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName in DEFAULT_DEPT_NAME or UPDATED_DEPT_NAME
        defaultDepartmentShouldBeFound("deptName.in=" + DEFAULT_DEPT_NAME + "," + UPDATED_DEPT_NAME);

        // Get all the departmentList where deptName equals to UPDATED_DEPT_NAME
        defaultDepartmentShouldNotBeFound("deptName.in=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName is not null
        defaultDepartmentShouldBeFound("deptName.specified=true");

        // Get all the departmentList where deptName is null
        defaultDepartmentShouldNotBeFound("deptName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepartmentsByDeptNameContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName contains DEFAULT_DEPT_NAME
        defaultDepartmentShouldBeFound("deptName.contains=" + DEFAULT_DEPT_NAME);

        // Get all the departmentList where deptName contains UPDATED_DEPT_NAME
        defaultDepartmentShouldNotBeFound("deptName.contains=" + UPDATED_DEPT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNameNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptName does not contain DEFAULT_DEPT_NAME
        defaultDepartmentShouldNotBeFound("deptName.doesNotContain=" + DEFAULT_DEPT_NAME);

        // Get all the departmentList where deptName does not contain UPDATED_DEPT_NAME
        defaultDepartmentShouldBeFound("deptName.doesNotContain=" + UPDATED_DEPT_NAME);
    }


    @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote equals to DEFAULT_DEPT_NOTE
        defaultDepartmentShouldBeFound("deptNote.equals=" + DEFAULT_DEPT_NOTE);

        // Get all the departmentList where deptNote equals to UPDATED_DEPT_NOTE
        defaultDepartmentShouldNotBeFound("deptNote.equals=" + UPDATED_DEPT_NOTE);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote not equals to DEFAULT_DEPT_NOTE
        defaultDepartmentShouldNotBeFound("deptNote.notEquals=" + DEFAULT_DEPT_NOTE);

        // Get all the departmentList where deptNote not equals to UPDATED_DEPT_NOTE
        defaultDepartmentShouldBeFound("deptNote.notEquals=" + UPDATED_DEPT_NOTE);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteIsInShouldWork() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote in DEFAULT_DEPT_NOTE or UPDATED_DEPT_NOTE
        defaultDepartmentShouldBeFound("deptNote.in=" + DEFAULT_DEPT_NOTE + "," + UPDATED_DEPT_NOTE);

        // Get all the departmentList where deptNote equals to UPDATED_DEPT_NOTE
        defaultDepartmentShouldNotBeFound("deptNote.in=" + UPDATED_DEPT_NOTE);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote is not null
        defaultDepartmentShouldBeFound("deptNote.specified=true");

        // Get all the departmentList where deptNote is null
        defaultDepartmentShouldNotBeFound("deptNote.specified=false");
    }
                @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote contains DEFAULT_DEPT_NOTE
        defaultDepartmentShouldBeFound("deptNote.contains=" + DEFAULT_DEPT_NOTE);

        // Get all the departmentList where deptNote contains UPDATED_DEPT_NOTE
        defaultDepartmentShouldNotBeFound("deptNote.contains=" + UPDATED_DEPT_NOTE);
    }

    @Test
    @Transactional
    public void getAllDepartmentsByDeptNoteNotContainsSomething() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        // Get all the departmentList where deptNote does not contain DEFAULT_DEPT_NOTE
        defaultDepartmentShouldNotBeFound("deptNote.doesNotContain=" + DEFAULT_DEPT_NOTE);

        // Get all the departmentList where deptNote does not contain UPDATED_DEPT_NOTE
        defaultDepartmentShouldBeFound("deptNote.doesNotContain=" + UPDATED_DEPT_NOTE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartmentShouldBeFound(String filter) throws Exception {
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].deptNote").value(hasItem(DEFAULT_DEPT_NOTE)));

        // Check, that the count call also returns 1
        restDepartmentMockMvc.perform(get("/api/departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartmentShouldNotBeFound(String filter) throws Exception {
        restDepartmentMockMvc.perform(get("/api/departments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartmentMockMvc.perform(get("/api/departments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepartment() throws Exception {
        // Get the department
        restDepartmentMockMvc.perform(get("/api/departments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Update the department
        Department updatedDepartment = departmentRepository.findById(department.getId()).get();
        // Disconnect from session so that the updates on updatedDepartment are not directly saved in db
        em.detach(updatedDepartment);
        updatedDepartment
            .deptName(UPDATED_DEPT_NAME)
            .deptNote(UPDATED_DEPT_NOTE);
        DepartmentDTO departmentDTO = departmentMapper.toDto(updatedDepartment);

        restDepartmentMockMvc.perform(put("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isOk());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);
        Department testDepartment = departmentList.get(departmentList.size() - 1);
        assertThat(testDepartment.getDeptName()).isEqualTo(UPDATED_DEPT_NAME);
        assertThat(testDepartment.getDeptNote()).isEqualTo(UPDATED_DEPT_NOTE);

        // Validate the Department in Elasticsearch
        verify(mockDepartmentSearchRepository, times(1)).save(testDepartment);
    }

    @Test
    @Transactional
    public void updateNonExistingDepartment() throws Exception {
        int databaseSizeBeforeUpdate = departmentRepository.findAll().size();

        // Create the Department
        DepartmentDTO departmentDTO = departmentMapper.toDto(department);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartmentMockMvc.perform(put("/api/departments")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(departmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Department in the database
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Department in Elasticsearch
        verify(mockDepartmentSearchRepository, times(0)).save(department);
    }

    @Test
    @Transactional
    public void deleteDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);

        int databaseSizeBeforeDelete = departmentRepository.findAll().size();

        // Delete the department
        restDepartmentMockMvc.perform(delete("/api/departments/{id}", department.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Department in Elasticsearch
        verify(mockDepartmentSearchRepository, times(1)).deleteById(department.getId());
    }

    @Test
    @Transactional
    public void searchDepartment() throws Exception {
        // Initialize the database
        departmentRepository.saveAndFlush(department);
        when(mockDepartmentSearchRepository.search(queryStringQuery("id:" + department.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(department), PageRequest.of(0, 1), 1));
        // Search the department
        restDepartmentMockMvc.perform(get("/api/_search/departments?query=id:" + department.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(department.getId().intValue())))
            .andExpect(jsonPath("$.[*].deptName").value(hasItem(DEFAULT_DEPT_NAME)))
            .andExpect(jsonPath("$.[*].deptNote").value(hasItem(DEFAULT_DEPT_NOTE)));
    }
}
