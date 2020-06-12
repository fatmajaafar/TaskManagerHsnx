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

    private static final String DEFAULT_EMPLOYEENAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEENAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEEFAX = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEEFAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEEADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEEADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEEEMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEEEMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EMPLOYEEHIREDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMPLOYEEHIREDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EMPLOYEEHIREDATE = LocalDate.ofEpochDay(-1L);

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
            .employeename(DEFAULT_EMPLOYEENAME)
            .employeephone(DEFAULT_EMPLOYEEPHONE)
            .employeefax(DEFAULT_EMPLOYEEFAX)
            .employeeaddress(DEFAULT_EMPLOYEEADDRESS)
            .employeeemail(DEFAULT_EMPLOYEEEMAIL)
            .employeehiredate(DEFAULT_EMPLOYEEHIREDATE);
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
            .employeename(UPDATED_EMPLOYEENAME)
            .employeephone(UPDATED_EMPLOYEEPHONE)
            .employeefax(UPDATED_EMPLOYEEFAX)
            .employeeaddress(UPDATED_EMPLOYEEADDRESS)
            .employeeemail(UPDATED_EMPLOYEEEMAIL)
            .employeehiredate(UPDATED_EMPLOYEEHIREDATE);
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
        assertThat(testEmployee.getEmployeename()).isEqualTo(DEFAULT_EMPLOYEENAME);
        assertThat(testEmployee.getEmployeephone()).isEqualTo(DEFAULT_EMPLOYEEPHONE);
        assertThat(testEmployee.getEmployeefax()).isEqualTo(DEFAULT_EMPLOYEEFAX);
        assertThat(testEmployee.getEmployeeaddress()).isEqualTo(DEFAULT_EMPLOYEEADDRESS);
        assertThat(testEmployee.getEmployeeemail()).isEqualTo(DEFAULT_EMPLOYEEEMAIL);
        assertThat(testEmployee.getEmployeehiredate()).isEqualTo(DEFAULT_EMPLOYEEHIREDATE);

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
    public void checkEmployeenameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setEmployeename(null);

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
            .andExpect(jsonPath("$.[*].employeename").value(hasItem(DEFAULT_EMPLOYEENAME)))
            .andExpect(jsonPath("$.[*].employeephone").value(hasItem(DEFAULT_EMPLOYEEPHONE)))
            .andExpect(jsonPath("$.[*].employeefax").value(hasItem(DEFAULT_EMPLOYEEFAX)))
            .andExpect(jsonPath("$.[*].employeeaddress").value(hasItem(DEFAULT_EMPLOYEEADDRESS)))
            .andExpect(jsonPath("$.[*].employeeemail").value(hasItem(DEFAULT_EMPLOYEEEMAIL)))
            .andExpect(jsonPath("$.[*].employeehiredate").value(hasItem(DEFAULT_EMPLOYEEHIREDATE.toString())));
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
            .andExpect(jsonPath("$.employeename").value(DEFAULT_EMPLOYEENAME))
            .andExpect(jsonPath("$.employeephone").value(DEFAULT_EMPLOYEEPHONE))
            .andExpect(jsonPath("$.employeefax").value(DEFAULT_EMPLOYEEFAX))
            .andExpect(jsonPath("$.employeeaddress").value(DEFAULT_EMPLOYEEADDRESS))
            .andExpect(jsonPath("$.employeeemail").value(DEFAULT_EMPLOYEEEMAIL))
            .andExpect(jsonPath("$.employeehiredate").value(DEFAULT_EMPLOYEEHIREDATE.toString()));
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
    public void getAllEmployeesByEmployeenameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename equals to DEFAULT_EMPLOYEENAME
        defaultEmployeeShouldBeFound("employeename.equals=" + DEFAULT_EMPLOYEENAME);

        // Get all the employeeList where employeename equals to UPDATED_EMPLOYEENAME
        defaultEmployeeShouldNotBeFound("employeename.equals=" + UPDATED_EMPLOYEENAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeenameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename not equals to DEFAULT_EMPLOYEENAME
        defaultEmployeeShouldNotBeFound("employeename.notEquals=" + DEFAULT_EMPLOYEENAME);

        // Get all the employeeList where employeename not equals to UPDATED_EMPLOYEENAME
        defaultEmployeeShouldBeFound("employeename.notEquals=" + UPDATED_EMPLOYEENAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeenameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename in DEFAULT_EMPLOYEENAME or UPDATED_EMPLOYEENAME
        defaultEmployeeShouldBeFound("employeename.in=" + DEFAULT_EMPLOYEENAME + "," + UPDATED_EMPLOYEENAME);

        // Get all the employeeList where employeename equals to UPDATED_EMPLOYEENAME
        defaultEmployeeShouldNotBeFound("employeename.in=" + UPDATED_EMPLOYEENAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeenameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename is not null
        defaultEmployeeShouldBeFound("employeename.specified=true");

        // Get all the employeeList where employeename is null
        defaultEmployeeShouldNotBeFound("employeename.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByEmployeenameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename contains DEFAULT_EMPLOYEENAME
        defaultEmployeeShouldBeFound("employeename.contains=" + DEFAULT_EMPLOYEENAME);

        // Get all the employeeList where employeename contains UPDATED_EMPLOYEENAME
        defaultEmployeeShouldNotBeFound("employeename.contains=" + UPDATED_EMPLOYEENAME);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeenameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeename does not contain DEFAULT_EMPLOYEENAME
        defaultEmployeeShouldNotBeFound("employeename.doesNotContain=" + DEFAULT_EMPLOYEENAME);

        // Get all the employeeList where employeename does not contain UPDATED_EMPLOYEENAME
        defaultEmployeeShouldBeFound("employeename.doesNotContain=" + UPDATED_EMPLOYEENAME);
    }


    @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone equals to DEFAULT_EMPLOYEEPHONE
        defaultEmployeeShouldBeFound("employeephone.equals=" + DEFAULT_EMPLOYEEPHONE);

        // Get all the employeeList where employeephone equals to UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldNotBeFound("employeephone.equals=" + UPDATED_EMPLOYEEPHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone not equals to DEFAULT_EMPLOYEEPHONE
        defaultEmployeeShouldNotBeFound("employeephone.notEquals=" + DEFAULT_EMPLOYEEPHONE);

        // Get all the employeeList where employeephone not equals to UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldBeFound("employeephone.notEquals=" + UPDATED_EMPLOYEEPHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone in DEFAULT_EMPLOYEEPHONE or UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldBeFound("employeephone.in=" + DEFAULT_EMPLOYEEPHONE + "," + UPDATED_EMPLOYEEPHONE);

        // Get all the employeeList where employeephone equals to UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldNotBeFound("employeephone.in=" + UPDATED_EMPLOYEEPHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone is not null
        defaultEmployeeShouldBeFound("employeephone.specified=true");

        // Get all the employeeList where employeephone is null
        defaultEmployeeShouldNotBeFound("employeephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone contains DEFAULT_EMPLOYEEPHONE
        defaultEmployeeShouldBeFound("employeephone.contains=" + DEFAULT_EMPLOYEEPHONE);

        // Get all the employeeList where employeephone contains UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldNotBeFound("employeephone.contains=" + UPDATED_EMPLOYEEPHONE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeephoneNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeephone does not contain DEFAULT_EMPLOYEEPHONE
        defaultEmployeeShouldNotBeFound("employeephone.doesNotContain=" + DEFAULT_EMPLOYEEPHONE);

        // Get all the employeeList where employeephone does not contain UPDATED_EMPLOYEEPHONE
        defaultEmployeeShouldBeFound("employeephone.doesNotContain=" + UPDATED_EMPLOYEEPHONE);
    }


    @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax equals to DEFAULT_EMPLOYEEFAX
        defaultEmployeeShouldBeFound("employeefax.equals=" + DEFAULT_EMPLOYEEFAX);

        // Get all the employeeList where employeefax equals to UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldNotBeFound("employeefax.equals=" + UPDATED_EMPLOYEEFAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax not equals to DEFAULT_EMPLOYEEFAX
        defaultEmployeeShouldNotBeFound("employeefax.notEquals=" + DEFAULT_EMPLOYEEFAX);

        // Get all the employeeList where employeefax not equals to UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldBeFound("employeefax.notEquals=" + UPDATED_EMPLOYEEFAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax in DEFAULT_EMPLOYEEFAX or UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldBeFound("employeefax.in=" + DEFAULT_EMPLOYEEFAX + "," + UPDATED_EMPLOYEEFAX);

        // Get all the employeeList where employeefax equals to UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldNotBeFound("employeefax.in=" + UPDATED_EMPLOYEEFAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax is not null
        defaultEmployeeShouldBeFound("employeefax.specified=true");

        // Get all the employeeList where employeefax is null
        defaultEmployeeShouldNotBeFound("employeefax.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax contains DEFAULT_EMPLOYEEFAX
        defaultEmployeeShouldBeFound("employeefax.contains=" + DEFAULT_EMPLOYEEFAX);

        // Get all the employeeList where employeefax contains UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldNotBeFound("employeefax.contains=" + UPDATED_EMPLOYEEFAX);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeefaxNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeefax does not contain DEFAULT_EMPLOYEEFAX
        defaultEmployeeShouldNotBeFound("employeefax.doesNotContain=" + DEFAULT_EMPLOYEEFAX);

        // Get all the employeeList where employeefax does not contain UPDATED_EMPLOYEEFAX
        defaultEmployeeShouldBeFound("employeefax.doesNotContain=" + UPDATED_EMPLOYEEFAX);
    }


    @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress equals to DEFAULT_EMPLOYEEADDRESS
        defaultEmployeeShouldBeFound("employeeaddress.equals=" + DEFAULT_EMPLOYEEADDRESS);

        // Get all the employeeList where employeeaddress equals to UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldNotBeFound("employeeaddress.equals=" + UPDATED_EMPLOYEEADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress not equals to DEFAULT_EMPLOYEEADDRESS
        defaultEmployeeShouldNotBeFound("employeeaddress.notEquals=" + DEFAULT_EMPLOYEEADDRESS);

        // Get all the employeeList where employeeaddress not equals to UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldBeFound("employeeaddress.notEquals=" + UPDATED_EMPLOYEEADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress in DEFAULT_EMPLOYEEADDRESS or UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldBeFound("employeeaddress.in=" + DEFAULT_EMPLOYEEADDRESS + "," + UPDATED_EMPLOYEEADDRESS);

        // Get all the employeeList where employeeaddress equals to UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldNotBeFound("employeeaddress.in=" + UPDATED_EMPLOYEEADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress is not null
        defaultEmployeeShouldBeFound("employeeaddress.specified=true");

        // Get all the employeeList where employeeaddress is null
        defaultEmployeeShouldNotBeFound("employeeaddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress contains DEFAULT_EMPLOYEEADDRESS
        defaultEmployeeShouldBeFound("employeeaddress.contains=" + DEFAULT_EMPLOYEEADDRESS);

        // Get all the employeeList where employeeaddress contains UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldNotBeFound("employeeaddress.contains=" + UPDATED_EMPLOYEEADDRESS);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeaddressNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeaddress does not contain DEFAULT_EMPLOYEEADDRESS
        defaultEmployeeShouldNotBeFound("employeeaddress.doesNotContain=" + DEFAULT_EMPLOYEEADDRESS);

        // Get all the employeeList where employeeaddress does not contain UPDATED_EMPLOYEEADDRESS
        defaultEmployeeShouldBeFound("employeeaddress.doesNotContain=" + UPDATED_EMPLOYEEADDRESS);
    }


    @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail equals to DEFAULT_EMPLOYEEEMAIL
        defaultEmployeeShouldBeFound("employeeemail.equals=" + DEFAULT_EMPLOYEEEMAIL);

        // Get all the employeeList where employeeemail equals to UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldNotBeFound("employeeemail.equals=" + UPDATED_EMPLOYEEEMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail not equals to DEFAULT_EMPLOYEEEMAIL
        defaultEmployeeShouldNotBeFound("employeeemail.notEquals=" + DEFAULT_EMPLOYEEEMAIL);

        // Get all the employeeList where employeeemail not equals to UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldBeFound("employeeemail.notEquals=" + UPDATED_EMPLOYEEEMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail in DEFAULT_EMPLOYEEEMAIL or UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldBeFound("employeeemail.in=" + DEFAULT_EMPLOYEEEMAIL + "," + UPDATED_EMPLOYEEEMAIL);

        // Get all the employeeList where employeeemail equals to UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldNotBeFound("employeeemail.in=" + UPDATED_EMPLOYEEEMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail is not null
        defaultEmployeeShouldBeFound("employeeemail.specified=true");

        // Get all the employeeList where employeeemail is null
        defaultEmployeeShouldNotBeFound("employeeemail.specified=false");
    }
                @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail contains DEFAULT_EMPLOYEEEMAIL
        defaultEmployeeShouldBeFound("employeeemail.contains=" + DEFAULT_EMPLOYEEEMAIL);

        // Get all the employeeList where employeeemail contains UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldNotBeFound("employeeemail.contains=" + UPDATED_EMPLOYEEEMAIL);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeeemailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeemail does not contain DEFAULT_EMPLOYEEEMAIL
        defaultEmployeeShouldNotBeFound("employeeemail.doesNotContain=" + DEFAULT_EMPLOYEEEMAIL);

        // Get all the employeeList where employeeemail does not contain UPDATED_EMPLOYEEEMAIL
        defaultEmployeeShouldBeFound("employeeemail.doesNotContain=" + UPDATED_EMPLOYEEEMAIL);
    }


    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate equals to DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.equals=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate equals to UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.equals=" + UPDATED_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate not equals to DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.notEquals=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate not equals to UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.notEquals=" + UPDATED_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate in DEFAULT_EMPLOYEEHIREDATE or UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.in=" + DEFAULT_EMPLOYEEHIREDATE + "," + UPDATED_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate equals to UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.in=" + UPDATED_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate is not null
        defaultEmployeeShouldBeFound("employeehiredate.specified=true");

        // Get all the employeeList where employeehiredate is null
        defaultEmployeeShouldNotBeFound("employeehiredate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate is greater than or equal to DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.greaterThanOrEqual=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate is greater than or equal to UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.greaterThanOrEqual=" + UPDATED_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate is less than or equal to DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.lessThanOrEqual=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate is less than or equal to SMALLER_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.lessThanOrEqual=" + SMALLER_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate is less than DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.lessThan=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate is less than UPDATED_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.lessThan=" + UPDATED_EMPLOYEEHIREDATE);
    }

    @Test
    @Transactional
    public void getAllEmployeesByEmployeehiredateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeehiredate is greater than DEFAULT_EMPLOYEEHIREDATE
        defaultEmployeeShouldNotBeFound("employeehiredate.greaterThan=" + DEFAULT_EMPLOYEEHIREDATE);

        // Get all the employeeList where employeehiredate is greater than SMALLER_EMPLOYEEHIREDATE
        defaultEmployeeShouldBeFound("employeehiredate.greaterThan=" + SMALLER_EMPLOYEEHIREDATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeename").value(hasItem(DEFAULT_EMPLOYEENAME)))
            .andExpect(jsonPath("$.[*].employeephone").value(hasItem(DEFAULT_EMPLOYEEPHONE)))
            .andExpect(jsonPath("$.[*].employeefax").value(hasItem(DEFAULT_EMPLOYEEFAX)))
            .andExpect(jsonPath("$.[*].employeeaddress").value(hasItem(DEFAULT_EMPLOYEEADDRESS)))
            .andExpect(jsonPath("$.[*].employeeemail").value(hasItem(DEFAULT_EMPLOYEEEMAIL)))
            .andExpect(jsonPath("$.[*].employeehiredate").value(hasItem(DEFAULT_EMPLOYEEHIREDATE.toString())));

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
            .employeename(UPDATED_EMPLOYEENAME)
            .employeephone(UPDATED_EMPLOYEEPHONE)
            .employeefax(UPDATED_EMPLOYEEFAX)
            .employeeaddress(UPDATED_EMPLOYEEADDRESS)
            .employeeemail(UPDATED_EMPLOYEEEMAIL)
            .employeehiredate(UPDATED_EMPLOYEEHIREDATE);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeename()).isEqualTo(UPDATED_EMPLOYEENAME);
        assertThat(testEmployee.getEmployeephone()).isEqualTo(UPDATED_EMPLOYEEPHONE);
        assertThat(testEmployee.getEmployeefax()).isEqualTo(UPDATED_EMPLOYEEFAX);
        assertThat(testEmployee.getEmployeeaddress()).isEqualTo(UPDATED_EMPLOYEEADDRESS);
        assertThat(testEmployee.getEmployeeemail()).isEqualTo(UPDATED_EMPLOYEEEMAIL);
        assertThat(testEmployee.getEmployeehiredate()).isEqualTo(UPDATED_EMPLOYEEHIREDATE);

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
            .andExpect(jsonPath("$.[*].employeename").value(hasItem(DEFAULT_EMPLOYEENAME)))
            .andExpect(jsonPath("$.[*].employeephone").value(hasItem(DEFAULT_EMPLOYEEPHONE)))
            .andExpect(jsonPath("$.[*].employeefax").value(hasItem(DEFAULT_EMPLOYEEFAX)))
            .andExpect(jsonPath("$.[*].employeeaddress").value(hasItem(DEFAULT_EMPLOYEEADDRESS)))
            .andExpect(jsonPath("$.[*].employeeemail").value(hasItem(DEFAULT_EMPLOYEEEMAIL)))
            .andExpect(jsonPath("$.[*].employeehiredate").value(hasItem(DEFAULT_EMPLOYEEHIREDATE.toString())));
    }
}
