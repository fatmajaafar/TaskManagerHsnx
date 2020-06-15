package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Country;
import com.pfe.hsnx.repository.CountryRepository;
import com.pfe.hsnx.repository.search.CountrySearchRepository;
import com.pfe.hsnx.service.CountryService;
import com.pfe.hsnx.service.dto.CountryDTO;
import com.pfe.hsnx.service.mapper.CountryMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.CountryCriteria;
import com.pfe.hsnx.service.CountryQueryService;

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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class CountryResourceIT {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private CountryService countryService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.CountrySearchRepositoryMockConfiguration
     */
    @Autowired
    private CountrySearchRepository mockCountrySearchRepository;

    @Autowired
    private CountryQueryService countryQueryService;

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

    private MockMvc restCountryMockMvc;

    private Country country;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CountryResource countryResource = new CountryResource(countryService, countryQueryService);
        this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
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
    public static Country createEntity(EntityManager em) {
        Country country = new Country()
            .countryName(DEFAULT_COUNTRY_NAME)
            .countryCode(DEFAULT_COUNTRY_CODE);
        return country;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country()
            .countryName(UPDATED_COUNTRY_NAME)
            .countryCode(UPDATED_COUNTRY_CODE);
        return country;
    }

    @BeforeEach
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(1)).save(testCountry);
    }

    @Test
    @Transactional
    public void createCountryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country with an existing ID
        country.setId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }


    @Test
    @Transactional
    public void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc.perform(post("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)));
    }
    
    @Test
    @Transactional
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE));
    }


    @Test
    @Transactional
    public void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        Long id = country.getId();

        defaultCountryShouldBeFound("id.equals=" + id);
        defaultCountryShouldNotBeFound("id.notEquals=" + id);

        defaultCountryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.greaterThan=" + id);

        defaultCountryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCountriesByCountryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName equals to DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.equals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.equals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName not equals to DEFAULT_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.notEquals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName not equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.notEquals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName in DEFAULT_COUNTRY_NAME or UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.in=" + DEFAULT_COUNTRY_NAME + "," + UPDATED_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.in=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName is not null
        defaultCountryShouldBeFound("countryName.specified=true");

        // Get all the countryList where countryName is null
        defaultCountryShouldNotBeFound("countryName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCountriesByCountryNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName contains DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.contains=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName contains UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.contains=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName does not contain DEFAULT_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.doesNotContain=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName does not contain UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.doesNotContain=" + UPDATED_COUNTRY_NAME);
    }


    @Test
    @Transactional
    public void getAllCountriesByCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode equals to DEFAULT_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.equals=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.equals=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode not equals to DEFAULT_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.notEquals=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode not equals to UPDATED_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.notEquals=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode in DEFAULT_COUNTRY_CODE or UPDATED_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.in=" + DEFAULT_COUNTRY_CODE + "," + UPDATED_COUNTRY_CODE);

        // Get all the countryList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.in=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode is not null
        defaultCountryShouldBeFound("countryCode.specified=true");

        // Get all the countryList where countryCode is null
        defaultCountryShouldNotBeFound("countryCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllCountriesByCountryCodeContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode contains DEFAULT_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.contains=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode contains UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.contains=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    public void getAllCountriesByCountryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode does not contain DEFAULT_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.doesNotContain=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode does not contain UPDATED_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.doesNotContain=" + UPDATED_COUNTRY_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)));

        // Check, that the count call also returns 1
        restCountryMockMvc.perform(get("/api/countries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc.perform(get("/api/countries/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry
            .countryName(UPDATED_COUNTRY_NAME)
            .countryCode(UPDATED_COUNTRY_CODE);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(1)).save(testCountry);
    }

    @Test
    @Transactional
    public void updateNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc.perform(put("/api/countries")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(0)).save(country);
    }

    @Test
    @Transactional
    public void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc.perform(delete("/api/countries/{id}", country.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Country in Elasticsearch
        verify(mockCountrySearchRepository, times(1)).deleteById(country.getId());
    }

    @Test
    @Transactional
    public void searchCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        when(mockCountrySearchRepository.search(queryStringQuery("id:" + country.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(country), PageRequest.of(0, 1), 1));
        // Search the country
        restCountryMockMvc.perform(get("/api/_search/countries?query=id:" + country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)));
    }
}
