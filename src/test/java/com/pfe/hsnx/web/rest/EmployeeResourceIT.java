package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Employee;
import com.pfe.hsnx.repository.EmployeeRepository;
import com.pfe.hsnx.repository.search.EmployeeSearchRepository;
import com.pfe.hsnx.service.EmployeeService;
import com.pfe.hsnx.service.dto.EmployeeDTO;
import com.pfe.hsnx.service.mapper.EmployeeMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.EmployeeCriteria;
import com.pfe.hsnx.service.EmployeeQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class EmployeeResourceIT {

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_FAX = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BRANCH_HIREDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BRANCH_HIREDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BRANCH_HIREDATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeService employeeService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.EmployeeSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmployeeSearchRepository mockEmployeeSearchRepository;

    @Autowired
    private EmployeeQueryService employeeQueryService;

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

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeResource employeeResource = new EmployeeResource(employeeService, employeeQueryService);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
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
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .branchName(DEFAULT_BRANCH_NAME)
            .branchPhone(DEFAULT_BRANCH_PHONE)
            .branchFax(DEFAULT_BRANCH_FAX)
            .branchAddress(DEFAULT_BRANCH_ADDRESS)
            .branchEmail(DEFAULT_BRANCH_EMAIL)
            .branchHiredate(DEFAULT_BRANCH_HIREDATE);
        return employee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .branchName(UPDATED_BRANCH_NAME)
            .branchPhone(UPDATED_BRANCH_PHONE)
            .branchFax(UPDATED_BRANCH_FAX)
            .branchAddress(UPDATED_BRANCH_ADDRESS)
            .branchEmail(UPDATED_BRANCH_EMAIL)
            .branchHiredate(UPDATED_BRANCH_HIREDATE);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testEmployee.getBranchPhone()).isEqualTo(DEFAULT_BRANCH_PHONE);
        assertThat(testEmployee.getBranchFax()).isEqualTo(DEFAULT_BRANCH_FAX);
        assertThat(testEmployee.getBranchAddress()).isEqualTo(DEFAULT_BRANCH_ADDRESS);
        assertThat(testEmployee.getBranchEmail()).isEqualTo(DEFAULT_BRANCH_EMAIL);
        assertThat(testEmployee.getBranchHiredate()).isEqualTo(DEFAULT_BRANCH_HIREDATE);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    @Transactional
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }


    @Test
    @Transactional
    public void checkBranchNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setBranchName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchHiredate").value(hasItem(DEFAULT_BRANCH_HIREDATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME))
            .andExpect(jsonPath("$.branchPhone").value(DEFAULT_BRANCH_PHONE))
            .andExpect(jsonPath("$.branchFax").value(DEFAULT_BRANCH_FAX))
            .andExpect(jsonPath("$.branchAddress").value(DEFAULT_BRANCH_ADDRESS))
            .andExpect(jsonPath("$.branchEmail").value(DEFAULT_BRANCH_EMAIL))
            .andExpect(jsonPath("$.branchHiredate").value(DEFAULT_BRANCH_HIREDATE.toString()));
    }


    @Test
    @Transactional
    public void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName equals to DEFAULT_BRANCH_NAME
        defaultEmployeeShouldBeFound("branchName.equals=" + DEFAULT_BRANCH_NAME);

        // Get all the employeeList where branchName equals to UPDATED_BRANCH_NAME
        defaultEmployeeShouldNotBeFound("branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName not equals to DEFAULT_BRANCH_NAME
        defaultEmployeeShouldNotBeFound("branchName.notEquals=" + DEFAULT_BRANCH_NAME);

        // Get all the employeeList where branchName not equals to UPDATED_BRANCH_NAME
        defaultEmployeeShouldBeFound("branchName.notEquals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName in DEFAULT_BRANCH_NAME or UPDATED_BRANCH_NAME
        defaultEmployeeShouldBeFound("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME);

        // Get all the employeeList where branchName equals to UPDATED_BRANCH_NAME
        defaultEmployeeShouldNotBeFound("branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName is not null
        defaultEmployeeShouldBeFound("branchName.specified=true");

        // Get all the employeeList where branchName is null
        defaultEmployeeShouldNotBeFound("branchName.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName contains DEFAULT_BRANCH_NAME
        defaultEmployeeShouldBeFound("branchName.contains=" + DEFAULT_BRANCH_NAME);

        // Get all the employeeList where branchName contains UPDATED_BRANCH_NAME
        defaultEmployeeShouldNotBeFound("branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchName does not contain DEFAULT_BRANCH_NAME
        defaultEmployeeShouldNotBeFound("branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);

        // Get all the employeeList where branchName does not contain UPDATED_BRANCH_NAME
        defaultEmployeeShouldBeFound("branchName.doesNotContain=" + UPDATED_BRANCH_NAME);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone equals to DEFAULT_BRANCH_PHONE
        defaultEmployeeShouldBeFound("branchPhone.equals=" + DEFAULT_BRANCH_PHONE);

        // Get all the employeeList where branchPhone equals to UPDATED_BRANCH_PHONE
        defaultEmployeeShouldNotBeFound("branchPhone.equals=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone not equals to DEFAULT_BRANCH_PHONE
        defaultEmployeeShouldNotBeFound("branchPhone.notEquals=" + DEFAULT_BRANCH_PHONE);

        // Get all the employeeList where branchPhone not equals to UPDATED_BRANCH_PHONE
        defaultEmployeeShouldBeFound("branchPhone.notEquals=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone in DEFAULT_BRANCH_PHONE or UPDATED_BRANCH_PHONE
        defaultEmployeeShouldBeFound("branchPhone.in=" + DEFAULT_BRANCH_PHONE + "," + UPDATED_BRANCH_PHONE);

        // Get all the employeeList where branchPhone equals to UPDATED_BRANCH_PHONE
        defaultEmployeeShouldNotBeFound("branchPhone.in=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone is not null
        defaultEmployeeShouldBeFound("branchPhone.specified=true");

        // Get all the employeeList where branchPhone is null
        defaultEmployeeShouldNotBeFound("branchPhone.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone contains DEFAULT_BRANCH_PHONE
        defaultEmployeeShouldBeFound("branchPhone.contains=" + DEFAULT_BRANCH_PHONE);

        // Get all the employeeList where branchPhone contains UPDATED_BRANCH_PHONE
        defaultEmployeeShouldNotBeFound("branchPhone.contains=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchPhone does not contain DEFAULT_BRANCH_PHONE
        defaultEmployeeShouldNotBeFound("branchPhone.doesNotContain=" + DEFAULT_BRANCH_PHONE);

        // Get all the employeeList where branchPhone does not contain UPDATED_BRANCH_PHONE
        defaultEmployeeShouldBeFound("branchPhone.doesNotContain=" + UPDATED_BRANCH_PHONE);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax equals to DEFAULT_BRANCH_FAX
        defaultEmployeeShouldBeFound("branchFax.equals=" + DEFAULT_BRANCH_FAX);

        // Get all the employeeList where branchFax equals to UPDATED_BRANCH_FAX
        defaultEmployeeShouldNotBeFound("branchFax.equals=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax not equals to DEFAULT_BRANCH_FAX
        defaultEmployeeShouldNotBeFound("branchFax.notEquals=" + DEFAULT_BRANCH_FAX);

        // Get all the employeeList where branchFax not equals to UPDATED_BRANCH_FAX
        defaultEmployeeShouldBeFound("branchFax.notEquals=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchFaxIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax in DEFAULT_BRANCH_FAX or UPDATED_BRANCH_FAX
        defaultEmployeeShouldBeFound("branchFax.in=" + DEFAULT_BRANCH_FAX + "," + UPDATED_BRANCH_FAX);

        // Get all the employeeList where branchFax equals to UPDATED_BRANCH_FAX
        defaultEmployeeShouldNotBeFound("branchFax.in=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax is not null
        defaultEmployeeShouldBeFound("branchFax.specified=true");

        // Get all the employeeList where branchFax is null
        defaultEmployeeShouldNotBeFound("branchFax.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByBranchFaxContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax contains DEFAULT_BRANCH_FAX
        defaultEmployeeShouldBeFound("branchFax.contains=" + DEFAULT_BRANCH_FAX);

        // Get all the employeeList where branchFax contains UPDATED_BRANCH_FAX
        defaultEmployeeShouldNotBeFound("branchFax.contains=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchFaxNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchFax does not contain DEFAULT_BRANCH_FAX
        defaultEmployeeShouldNotBeFound("branchFax.doesNotContain=" + DEFAULT_BRANCH_FAX);

        // Get all the employeeList where branchFax does not contain UPDATED_BRANCH_FAX
        defaultEmployeeShouldBeFound("branchFax.doesNotContain=" + UPDATED_BRANCH_FAX);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress equals to DEFAULT_BRANCH_ADDRESS
        defaultEmployeeShouldBeFound("branchAddress.equals=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the employeeList where branchAddress equals to UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldNotBeFound("branchAddress.equals=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress not equals to DEFAULT_BRANCH_ADDRESS
        defaultEmployeeShouldNotBeFound("branchAddress.notEquals=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the employeeList where branchAddress not equals to UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldBeFound("branchAddress.notEquals=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchAddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress in DEFAULT_BRANCH_ADDRESS or UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldBeFound("branchAddress.in=" + DEFAULT_BRANCH_ADDRESS + "," + UPDATED_BRANCH_ADDRESS);

        // Get all the employeeList where branchAddress equals to UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldNotBeFound("branchAddress.in=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress is not null
        defaultEmployeeShouldBeFound("branchAddress.specified=true");

        // Get all the employeeList where branchAddress is null
        defaultEmployeeShouldNotBeFound("branchAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByBranchAddressContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress contains DEFAULT_BRANCH_ADDRESS
        defaultEmployeeShouldBeFound("branchAddress.contains=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the employeeList where branchAddress contains UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldNotBeFound("branchAddress.contains=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchAddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchAddress does not contain DEFAULT_BRANCH_ADDRESS
        defaultEmployeeShouldNotBeFound("branchAddress.doesNotContain=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the employeeList where branchAddress does not contain UPDATED_BRANCH_ADDRESS
        defaultEmployeeShouldBeFound("branchAddress.doesNotContain=" + UPDATED_BRANCH_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail equals to DEFAULT_BRANCH_EMAIL
        defaultEmployeeShouldBeFound("branchEmail.equals=" + DEFAULT_BRANCH_EMAIL);

        // Get all the employeeList where branchEmail equals to UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldNotBeFound("branchEmail.equals=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail not equals to DEFAULT_BRANCH_EMAIL
        defaultEmployeeShouldNotBeFound("branchEmail.notEquals=" + DEFAULT_BRANCH_EMAIL);

        // Get all the employeeList where branchEmail not equals to UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldBeFound("branchEmail.notEquals=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail in DEFAULT_BRANCH_EMAIL or UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldBeFound("branchEmail.in=" + DEFAULT_BRANCH_EMAIL + "," + UPDATED_BRANCH_EMAIL);

        // Get all the employeeList where branchEmail equals to UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldNotBeFound("branchEmail.in=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail is not null
        defaultEmployeeShouldBeFound("branchEmail.specified=true");

        // Get all the employeeList where branchEmail is null
        defaultEmployeeShouldNotBeFound("branchEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByBranchEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail contains DEFAULT_BRANCH_EMAIL
        defaultEmployeeShouldBeFound("branchEmail.contains=" + DEFAULT_BRANCH_EMAIL);

        // Get all the employeeList where branchEmail contains UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldNotBeFound("branchEmail.contains=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchEmail does not contain DEFAULT_BRANCH_EMAIL
        defaultEmployeeShouldNotBeFound("branchEmail.doesNotContain=" + DEFAULT_BRANCH_EMAIL);

        // Get all the employeeList where branchEmail does not contain UPDATED_BRANCH_EMAIL
        defaultEmployeeShouldBeFound("branchEmail.doesNotContain=" + UPDATED_BRANCH_EMAIL);
    }


    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate equals to DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.equals=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate equals to UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.equals=" + UPDATED_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate not equals to DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.notEquals=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate not equals to UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.notEquals=" + UPDATED_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate in DEFAULT_BRANCH_HIREDATE or UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.in=" + DEFAULT_BRANCH_HIREDATE + "," + UPDATED_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate equals to UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.in=" + UPDATED_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate is not null
        defaultEmployeeShouldBeFound("branchHiredate.specified=true");

        // Get all the employeeList where branchHiredate is null
        defaultEmployeeShouldNotBeFound("branchHiredate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate is greater than or equal to DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.greaterThanOrEqual=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate is greater than or equal to UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.greaterThanOrEqual=" + UPDATED_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate is less than or equal to DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.lessThanOrEqual=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate is less than or equal to SMALLER_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.lessThanOrEqual=" + SMALLER_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate is less than DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.lessThan=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate is less than UPDATED_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.lessThan=" + UPDATED_BRANCH_HIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByBranchHiredateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where branchHiredate is greater than DEFAULT_BRANCH_HIREDATE
        defaultEmployeeShouldNotBeFound("branchHiredate.greaterThan=" + DEFAULT_BRANCH_HIREDATE);

        // Get all the employeeList where branchHiredate is greater than SMALLER_BRANCH_HIREDATE
        defaultEmployeeShouldBeFound("branchHiredate.greaterThan=" + SMALLER_BRANCH_HIREDATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchHiredate").value(hasItem(DEFAULT_BRANCH_HIREDATE.toString())));

        // Check, that the count call also returns 1
        restEmployeeMockMvc.perform(get("/api/employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc.perform(get("/api/employees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .branchName(UPDATED_BRANCH_NAME)
            .branchPhone(UPDATED_BRANCH_PHONE)
            .branchFax(UPDATED_BRANCH_FAX)
            .branchAddress(UPDATED_BRANCH_ADDRESS)
            .branchEmail(UPDATED_BRANCH_EMAIL)
            .branchHiredate(UPDATED_BRANCH_HIREDATE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testEmployee.getBranchPhone()).isEqualTo(UPDATED_BRANCH_PHONE);
        assertThat(testEmployee.getBranchFax()).isEqualTo(UPDATED_BRANCH_FAX);
        assertThat(testEmployee.getBranchAddress()).isEqualTo(UPDATED_BRANCH_ADDRESS);
        assertThat(testEmployee.getBranchEmail()).isEqualTo(UPDATED_BRANCH_EMAIL);
        assertThat(testEmployee.getBranchHiredate()).isEqualTo(UPDATED_BRANCH_HIREDATE);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }

    @Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).deleteById(employee.getId());
    }

    @Test
    @Transactional
    public void searchEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        when(mockEmployeeSearchRepository.search(queryStringQuery("id:" + employee.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(employee), PageRequest.of(0, 1), 1));
        // Search the employee
        restEmployeeMockMvc.perform(get("/api/_search/employees?query=id:" + employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchHiredate").value(hasItem(DEFAULT_BRANCH_HIREDATE.toString())));
    }
}
