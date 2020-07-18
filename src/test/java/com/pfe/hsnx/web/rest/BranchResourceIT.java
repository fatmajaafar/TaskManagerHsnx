package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Branch;
import com.pfe.hsnx.repository.BranchRepository;
import com.pfe.hsnx.repository.search.BranchSearchRepository;
import com.pfe.hsnx.service.BranchService;
import com.pfe.hsnx.service.dto.BranchDTO;
import com.pfe.hsnx.service.mapper.BranchMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.BranchCriteria;
import com.pfe.hsnx.service.BranchQueryService;

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
 * Integration tests for the {@link BranchResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class BranchResourceIT {

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

    private static final String DEFAULT_BRANCH_BANNER = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_BANNER = "BBBBBBBBBB";

    private static final Integer DEFAULT_BRANCH_DEFAULT_EXERCICE = 1;
    private static final Integer UPDATED_BRANCH_DEFAULT_EXERCICE = 2;
    private static final Integer SMALLER_BRANCH_DEFAULT_EXERCICE = 1 - 1;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private BranchService branchService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.BranchSearchRepositoryMockConfiguration
     */
    @Autowired
    private BranchSearchRepository mockBranchSearchRepository;

    @Autowired
    private BranchQueryService branchQueryService;

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

    private MockMvc restBranchMockMvc;

    private Branch branch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchResource branchResource = new BranchResource(branchService, branchQueryService);
        this.restBranchMockMvc = MockMvcBuilders.standaloneSetup(branchResource)
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
    public static Branch createEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchName(DEFAULT_BRANCH_NAME)
            .branchPhone(DEFAULT_BRANCH_PHONE)
            .branchFax(DEFAULT_BRANCH_FAX)
            .branchAddress(DEFAULT_BRANCH_ADDRESS)
            .branchEmail(DEFAULT_BRANCH_EMAIL)
            .branchBanner(DEFAULT_BRANCH_BANNER)
            .branchDefaultExercice(DEFAULT_BRANCH_DEFAULT_EXERCICE);
        return branch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchName(UPDATED_BRANCH_NAME)
            .branchPhone(UPDATED_BRANCH_PHONE)
            .branchFax(UPDATED_BRANCH_FAX)
            .branchAddress(UPDATED_BRANCH_ADDRESS)
            .branchEmail(UPDATED_BRANCH_EMAIL)
            .branchBanner(UPDATED_BRANCH_BANNER)
            .branchDefaultExercice(UPDATED_BRANCH_DEFAULT_EXERCICE);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testBranch.getBranchPhone()).isEqualTo(DEFAULT_BRANCH_PHONE);
        assertThat(testBranch.getBranchFax()).isEqualTo(DEFAULT_BRANCH_FAX);
        assertThat(testBranch.getBranchAddress()).isEqualTo(DEFAULT_BRANCH_ADDRESS);
        assertThat(testBranch.getBranchEmail()).isEqualTo(DEFAULT_BRANCH_EMAIL);
        assertThat(testBranch.getBranchBanner()).isEqualTo(DEFAULT_BRANCH_BANNER);
        assertThat(testBranch.getBranchDefaultExercice()).isEqualTo(DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).save(testBranch);
    }

    @Test
    @Transactional
    public void createBranchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch with an existing ID
        branch.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(0)).save(branch);
    }


    @Test
    @Transactional
    public void checkBranchNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setBranchName(null);

        // Create the Branch, which fails.
        BranchDTO branchDTO = branchMapper.toDto(branch);

        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchBanner").value(hasItem(DEFAULT_BRANCH_BANNER)))
            .andExpect(jsonPath("$.[*].branchDefaultExercice").value(hasItem(DEFAULT_BRANCH_DEFAULT_EXERCICE)));
    }
    
    @Test
    @Transactional
    public void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME))
            .andExpect(jsonPath("$.branchPhone").value(DEFAULT_BRANCH_PHONE))
            .andExpect(jsonPath("$.branchFax").value(DEFAULT_BRANCH_FAX))
            .andExpect(jsonPath("$.branchAddress").value(DEFAULT_BRANCH_ADDRESS))
            .andExpect(jsonPath("$.branchEmail").value(DEFAULT_BRANCH_EMAIL))
            .andExpect(jsonPath("$.branchBanner").value(DEFAULT_BRANCH_BANNER))
            .andExpect(jsonPath("$.branchDefaultExercice").value(DEFAULT_BRANCH_DEFAULT_EXERCICE));
    }


    @Test
    @Transactional
    public void getBranchesByIdFiltering() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        Long id = branch.getId();

        defaultBranchShouldBeFound("id.equals=" + id);
        defaultBranchShouldNotBeFound("id.notEquals=" + id);

        defaultBranchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.greaterThan=" + id);

        defaultBranchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBranchShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName equals to DEFAULT_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.equals=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName equals to UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName not equals to DEFAULT_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.notEquals=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName not equals to UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.notEquals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName in DEFAULT_BRANCH_NAME or UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME);

        // Get all the branchList where branchName equals to UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName is not null
        defaultBranchShouldBeFound("branchName.specified=true");

        // Get all the branchList where branchName is null
        defaultBranchShouldNotBeFound("branchName.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName contains DEFAULT_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.contains=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName contains UPDATED_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchName does not contain DEFAULT_BRANCH_NAME
        defaultBranchShouldNotBeFound("branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);

        // Get all the branchList where branchName does not contain UPDATED_BRANCH_NAME
        defaultBranchShouldBeFound("branchName.doesNotContain=" + UPDATED_BRANCH_NAME);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone equals to DEFAULT_BRANCH_PHONE
        defaultBranchShouldBeFound("branchPhone.equals=" + DEFAULT_BRANCH_PHONE);

        // Get all the branchList where branchPhone equals to UPDATED_BRANCH_PHONE
        defaultBranchShouldNotBeFound("branchPhone.equals=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone not equals to DEFAULT_BRANCH_PHONE
        defaultBranchShouldNotBeFound("branchPhone.notEquals=" + DEFAULT_BRANCH_PHONE);

        // Get all the branchList where branchPhone not equals to UPDATED_BRANCH_PHONE
        defaultBranchShouldBeFound("branchPhone.notEquals=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone in DEFAULT_BRANCH_PHONE or UPDATED_BRANCH_PHONE
        defaultBranchShouldBeFound("branchPhone.in=" + DEFAULT_BRANCH_PHONE + "," + UPDATED_BRANCH_PHONE);

        // Get all the branchList where branchPhone equals to UPDATED_BRANCH_PHONE
        defaultBranchShouldNotBeFound("branchPhone.in=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone is not null
        defaultBranchShouldBeFound("branchPhone.specified=true");

        // Get all the branchList where branchPhone is null
        defaultBranchShouldNotBeFound("branchPhone.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchPhoneContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone contains DEFAULT_BRANCH_PHONE
        defaultBranchShouldBeFound("branchPhone.contains=" + DEFAULT_BRANCH_PHONE);

        // Get all the branchList where branchPhone contains UPDATED_BRANCH_PHONE
        defaultBranchShouldNotBeFound("branchPhone.contains=" + UPDATED_BRANCH_PHONE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchPhone does not contain DEFAULT_BRANCH_PHONE
        defaultBranchShouldNotBeFound("branchPhone.doesNotContain=" + DEFAULT_BRANCH_PHONE);

        // Get all the branchList where branchPhone does not contain UPDATED_BRANCH_PHONE
        defaultBranchShouldBeFound("branchPhone.doesNotContain=" + UPDATED_BRANCH_PHONE);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax equals to DEFAULT_BRANCH_FAX
        defaultBranchShouldBeFound("branchFax.equals=" + DEFAULT_BRANCH_FAX);

        // Get all the branchList where branchFax equals to UPDATED_BRANCH_FAX
        defaultBranchShouldNotBeFound("branchFax.equals=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax not equals to DEFAULT_BRANCH_FAX
        defaultBranchShouldNotBeFound("branchFax.notEquals=" + DEFAULT_BRANCH_FAX);

        // Get all the branchList where branchFax not equals to UPDATED_BRANCH_FAX
        defaultBranchShouldBeFound("branchFax.notEquals=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchFaxIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax in DEFAULT_BRANCH_FAX or UPDATED_BRANCH_FAX
        defaultBranchShouldBeFound("branchFax.in=" + DEFAULT_BRANCH_FAX + "," + UPDATED_BRANCH_FAX);

        // Get all the branchList where branchFax equals to UPDATED_BRANCH_FAX
        defaultBranchShouldNotBeFound("branchFax.in=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax is not null
        defaultBranchShouldBeFound("branchFax.specified=true");

        // Get all the branchList where branchFax is null
        defaultBranchShouldNotBeFound("branchFax.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchFaxContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax contains DEFAULT_BRANCH_FAX
        defaultBranchShouldBeFound("branchFax.contains=" + DEFAULT_BRANCH_FAX);

        // Get all the branchList where branchFax contains UPDATED_BRANCH_FAX
        defaultBranchShouldNotBeFound("branchFax.contains=" + UPDATED_BRANCH_FAX);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchFaxNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchFax does not contain DEFAULT_BRANCH_FAX
        defaultBranchShouldNotBeFound("branchFax.doesNotContain=" + DEFAULT_BRANCH_FAX);

        // Get all the branchList where branchFax does not contain UPDATED_BRANCH_FAX
        defaultBranchShouldBeFound("branchFax.doesNotContain=" + UPDATED_BRANCH_FAX);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress equals to DEFAULT_BRANCH_ADDRESS
        defaultBranchShouldBeFound("branchAddress.equals=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the branchList where branchAddress equals to UPDATED_BRANCH_ADDRESS
        defaultBranchShouldNotBeFound("branchAddress.equals=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress not equals to DEFAULT_BRANCH_ADDRESS
        defaultBranchShouldNotBeFound("branchAddress.notEquals=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the branchList where branchAddress not equals to UPDATED_BRANCH_ADDRESS
        defaultBranchShouldBeFound("branchAddress.notEquals=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchAddressIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress in DEFAULT_BRANCH_ADDRESS or UPDATED_BRANCH_ADDRESS
        defaultBranchShouldBeFound("branchAddress.in=" + DEFAULT_BRANCH_ADDRESS + "," + UPDATED_BRANCH_ADDRESS);

        // Get all the branchList where branchAddress equals to UPDATED_BRANCH_ADDRESS
        defaultBranchShouldNotBeFound("branchAddress.in=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress is not null
        defaultBranchShouldBeFound("branchAddress.specified=true");

        // Get all the branchList where branchAddress is null
        defaultBranchShouldNotBeFound("branchAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchAddressContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress contains DEFAULT_BRANCH_ADDRESS
        defaultBranchShouldBeFound("branchAddress.contains=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the branchList where branchAddress contains UPDATED_BRANCH_ADDRESS
        defaultBranchShouldNotBeFound("branchAddress.contains=" + UPDATED_BRANCH_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchAddressNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchAddress does not contain DEFAULT_BRANCH_ADDRESS
        defaultBranchShouldNotBeFound("branchAddress.doesNotContain=" + DEFAULT_BRANCH_ADDRESS);

        // Get all the branchList where branchAddress does not contain UPDATED_BRANCH_ADDRESS
        defaultBranchShouldBeFound("branchAddress.doesNotContain=" + UPDATED_BRANCH_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail equals to DEFAULT_BRANCH_EMAIL
        defaultBranchShouldBeFound("branchEmail.equals=" + DEFAULT_BRANCH_EMAIL);

        // Get all the branchList where branchEmail equals to UPDATED_BRANCH_EMAIL
        defaultBranchShouldNotBeFound("branchEmail.equals=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail not equals to DEFAULT_BRANCH_EMAIL
        defaultBranchShouldNotBeFound("branchEmail.notEquals=" + DEFAULT_BRANCH_EMAIL);

        // Get all the branchList where branchEmail not equals to UPDATED_BRANCH_EMAIL
        defaultBranchShouldBeFound("branchEmail.notEquals=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchEmailIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail in DEFAULT_BRANCH_EMAIL or UPDATED_BRANCH_EMAIL
        defaultBranchShouldBeFound("branchEmail.in=" + DEFAULT_BRANCH_EMAIL + "," + UPDATED_BRANCH_EMAIL);

        // Get all the branchList where branchEmail equals to UPDATED_BRANCH_EMAIL
        defaultBranchShouldNotBeFound("branchEmail.in=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail is not null
        defaultBranchShouldBeFound("branchEmail.specified=true");

        // Get all the branchList where branchEmail is null
        defaultBranchShouldNotBeFound("branchEmail.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchEmailContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail contains DEFAULT_BRANCH_EMAIL
        defaultBranchShouldBeFound("branchEmail.contains=" + DEFAULT_BRANCH_EMAIL);

        // Get all the branchList where branchEmail contains UPDATED_BRANCH_EMAIL
        defaultBranchShouldNotBeFound("branchEmail.contains=" + UPDATED_BRANCH_EMAIL);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchEmailNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchEmail does not contain DEFAULT_BRANCH_EMAIL
        defaultBranchShouldNotBeFound("branchEmail.doesNotContain=" + DEFAULT_BRANCH_EMAIL);

        // Get all the branchList where branchEmail does not contain UPDATED_BRANCH_EMAIL
        defaultBranchShouldBeFound("branchEmail.doesNotContain=" + UPDATED_BRANCH_EMAIL);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchBannerIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner equals to DEFAULT_BRANCH_BANNER
        defaultBranchShouldBeFound("branchBanner.equals=" + DEFAULT_BRANCH_BANNER);

        // Get all the branchList where branchBanner equals to UPDATED_BRANCH_BANNER
        defaultBranchShouldNotBeFound("branchBanner.equals=" + UPDATED_BRANCH_BANNER);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchBannerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner not equals to DEFAULT_BRANCH_BANNER
        defaultBranchShouldNotBeFound("branchBanner.notEquals=" + DEFAULT_BRANCH_BANNER);

        // Get all the branchList where branchBanner not equals to UPDATED_BRANCH_BANNER
        defaultBranchShouldBeFound("branchBanner.notEquals=" + UPDATED_BRANCH_BANNER);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchBannerIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner in DEFAULT_BRANCH_BANNER or UPDATED_BRANCH_BANNER
        defaultBranchShouldBeFound("branchBanner.in=" + DEFAULT_BRANCH_BANNER + "," + UPDATED_BRANCH_BANNER);

        // Get all the branchList where branchBanner equals to UPDATED_BRANCH_BANNER
        defaultBranchShouldNotBeFound("branchBanner.in=" + UPDATED_BRANCH_BANNER);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchBannerIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner is not null
        defaultBranchShouldBeFound("branchBanner.specified=true");

        // Get all the branchList where branchBanner is null
        defaultBranchShouldNotBeFound("branchBanner.specified=false");
    }
                @Test
    @Transactional
    public void getAllBranchesByBranchBannerContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner contains DEFAULT_BRANCH_BANNER
        defaultBranchShouldBeFound("branchBanner.contains=" + DEFAULT_BRANCH_BANNER);

        // Get all the branchList where branchBanner contains UPDATED_BRANCH_BANNER
        defaultBranchShouldNotBeFound("branchBanner.contains=" + UPDATED_BRANCH_BANNER);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchBannerNotContainsSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchBanner does not contain DEFAULT_BRANCH_BANNER
        defaultBranchShouldNotBeFound("branchBanner.doesNotContain=" + DEFAULT_BRANCH_BANNER);

        // Get all the branchList where branchBanner does not contain UPDATED_BRANCH_BANNER
        defaultBranchShouldBeFound("branchBanner.doesNotContain=" + UPDATED_BRANCH_BANNER);
    }


    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice equals to DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.equals=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice equals to UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.equals=" + UPDATED_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice not equals to DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.notEquals=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice not equals to UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.notEquals=" + UPDATED_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsInShouldWork() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice in DEFAULT_BRANCH_DEFAULT_EXERCICE or UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.in=" + DEFAULT_BRANCH_DEFAULT_EXERCICE + "," + UPDATED_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice equals to UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.in=" + UPDATED_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsNullOrNotNull() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice is not null
        defaultBranchShouldBeFound("branchDefaultExercice.specified=true");

        // Get all the branchList where branchDefaultExercice is null
        defaultBranchShouldNotBeFound("branchDefaultExercice.specified=false");
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice is greater than or equal to DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.greaterThanOrEqual=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice is greater than or equal to UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.greaterThanOrEqual=" + UPDATED_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice is less than or equal to DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.lessThanOrEqual=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice is less than or equal to SMALLER_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.lessThanOrEqual=" + SMALLER_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsLessThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice is less than DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.lessThan=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice is less than UPDATED_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.lessThan=" + UPDATED_BRANCH_DEFAULT_EXERCICE);
    }

    @Test
    @Transactional
    public void getAllBranchesByBranchDefaultExerciceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList where branchDefaultExercice is greater than DEFAULT_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldNotBeFound("branchDefaultExercice.greaterThan=" + DEFAULT_BRANCH_DEFAULT_EXERCICE);

        // Get all the branchList where branchDefaultExercice is greater than SMALLER_BRANCH_DEFAULT_EXERCICE
        defaultBranchShouldBeFound("branchDefaultExercice.greaterThan=" + SMALLER_BRANCH_DEFAULT_EXERCICE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchShouldBeFound(String filter) throws Exception {
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchBanner").value(hasItem(DEFAULT_BRANCH_BANNER)))
            .andExpect(jsonPath("$.[*].branchDefaultExercice").value(hasItem(DEFAULT_BRANCH_DEFAULT_EXERCICE)));

        // Check, that the count call also returns 1
        restBranchMockMvc.perform(get("/api/branches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchShouldNotBeFound(String filter) throws Exception {
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchMockMvc.perform(get("/api/branches/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).get();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch
            .branchName(UPDATED_BRANCH_NAME)
            .branchPhone(UPDATED_BRANCH_PHONE)
            .branchFax(UPDATED_BRANCH_FAX)
            .branchAddress(UPDATED_BRANCH_ADDRESS)
            .branchEmail(UPDATED_BRANCH_EMAIL)
            .branchBanner(UPDATED_BRANCH_BANNER)
            .branchDefaultExercice(UPDATED_BRANCH_DEFAULT_EXERCICE);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranch);

        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testBranch.getBranchPhone()).isEqualTo(UPDATED_BRANCH_PHONE);
        assertThat(testBranch.getBranchFax()).isEqualTo(UPDATED_BRANCH_FAX);
        assertThat(testBranch.getBranchAddress()).isEqualTo(UPDATED_BRANCH_ADDRESS);
        assertThat(testBranch.getBranchEmail()).isEqualTo(UPDATED_BRANCH_EMAIL);
        assertThat(testBranch.getBranchBanner()).isEqualTo(UPDATED_BRANCH_BANNER);
        assertThat(testBranch.getBranchDefaultExercice()).isEqualTo(UPDATED_BRANCH_DEFAULT_EXERCICE);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).save(testBranch);
    }

    @Test
    @Transactional
    public void updateNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(0)).save(branch);
    }

    @Test
    @Transactional
    public void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc.perform(delete("/api/branches/{id}", branch.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).deleteById(branch.getId());
    }

    @Test
    @Transactional
    public void searchBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
        when(mockBranchSearchRepository.search(queryStringQuery("id:" + branch.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(branch), PageRequest.of(0, 1), 1));
        // Search the branch
        restBranchMockMvc.perform(get("/api/_search/branches?query=id:" + branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].branchPhone").value(hasItem(DEFAULT_BRANCH_PHONE)))
            .andExpect(jsonPath("$.[*].branchFax").value(hasItem(DEFAULT_BRANCH_FAX)))
            .andExpect(jsonPath("$.[*].branchAddress").value(hasItem(DEFAULT_BRANCH_ADDRESS)))
            .andExpect(jsonPath("$.[*].branchEmail").value(hasItem(DEFAULT_BRANCH_EMAIL)))
            .andExpect(jsonPath("$.[*].branchBanner").value(hasItem(DEFAULT_BRANCH_BANNER)))
            .andExpect(jsonPath("$.[*].branchDefaultExercice").value(hasItem(DEFAULT_BRANCH_DEFAULT_EXERCICE)));
    }
}
