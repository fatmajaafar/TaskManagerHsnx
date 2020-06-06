package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Event;
import com.pfe.hsnx.repository.EventRepository;
import com.pfe.hsnx.repository.search.EventSearchRepository;
import com.pfe.hsnx.service.EventService;
import com.pfe.hsnx.service.dto.EventDTO;
import com.pfe.hsnx.service.mapper.EventMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.EventCriteria;
import com.pfe.hsnx.service.EventQueryService;

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
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class EventResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_STARTTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_STARTDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STARTDATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_ENDTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_ENDDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENDDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENDDATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventService eventService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.EventSearchRepositoryMockConfiguration
     */
    @Autowired
    private EventSearchRepository mockEventSearchRepository;

    @Autowired
    private EventQueryService eventQueryService;

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

    private MockMvc restEventMockMvc;

    private Event event;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService, eventQueryService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
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
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .Description(DEFAULT_DESCRIPTION)
            .starttime(DEFAULT_STARTTIME)
            .startdate(DEFAULT_STARTDATE)
            .endtime(DEFAULT_ENDTIME)
            .enddate(DEFAULT_ENDDATE);
        return event;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .Description(UPDATED_DESCRIPTION)
            .starttime(UPDATED_STARTTIME)
            .startdate(UPDATED_STARTDATE)
            .endtime(UPDATED_ENDTIME)
            .enddate(UPDATED_ENDDATE);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvent.getStarttime()).isEqualTo(DEFAULT_STARTTIME);
        assertThat(testEvent.getStartdate()).isEqualTo(DEFAULT_STARTDATE);
        assertThat(testEvent.getEndtime()).isEqualTo(DEFAULT_ENDTIME);
        assertThat(testEvent.getEnddate()).isEqualTo(DEFAULT_ENDDATE);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(0)).save(event);
    }


    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setDescription(null);

        // Create the Event, which fails.
        EventDTO eventDTO = eventMapper.toDto(event);

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].Description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].starttime").value(hasItem(DEFAULT_STARTTIME.toString())))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(DEFAULT_STARTDATE.toString())))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME.toString())))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(DEFAULT_ENDDATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.Description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.starttime").value(DEFAULT_STARTTIME.toString()))
            .andExpect(jsonPath("$.startdate").value(DEFAULT_STARTDATE.toString()))
            .andExpect(jsonPath("$.endtime").value(DEFAULT_ENDTIME.toString()))
            .andExpect(jsonPath("$.enddate").value(DEFAULT_ENDDATE.toString()));
    }


    @Test
    @Transactional
    public void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        Long id = event.getId();

        defaultEventShouldBeFound("id.equals=" + id);
        defaultEventShouldNotBeFound("id.notEquals=" + id);

        defaultEventShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.greaterThan=" + id);

        defaultEventShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEventsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description equals to DEFAULT_DESCRIPTION
        defaultEventShouldBeFound("Description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where Description equals to UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("Description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description not equals to DEFAULT_DESCRIPTION
        defaultEventShouldNotBeFound("Description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where Description not equals to UPDATED_DESCRIPTION
        defaultEventShouldBeFound("Description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEventShouldBeFound("Description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the eventList where Description equals to UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("Description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description is not null
        defaultEventShouldBeFound("Description.specified=true");

        // Get all the eventList where Description is null
        defaultEventShouldNotBeFound("Description.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description contains DEFAULT_DESCRIPTION
        defaultEventShouldBeFound("Description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where Description contains UPDATED_DESCRIPTION
        defaultEventShouldNotBeFound("Description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllEventsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where Description does not contain DEFAULT_DESCRIPTION
        defaultEventShouldNotBeFound("Description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the eventList where Description does not contain UPDATED_DESCRIPTION
        defaultEventShouldBeFound("Description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllEventsByStarttimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where starttime equals to DEFAULT_STARTTIME
        defaultEventShouldBeFound("starttime.equals=" + DEFAULT_STARTTIME);

        // Get all the eventList where starttime equals to UPDATED_STARTTIME
        defaultEventShouldNotBeFound("starttime.equals=" + UPDATED_STARTTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByStarttimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where starttime not equals to DEFAULT_STARTTIME
        defaultEventShouldNotBeFound("starttime.notEquals=" + DEFAULT_STARTTIME);

        // Get all the eventList where starttime not equals to UPDATED_STARTTIME
        defaultEventShouldBeFound("starttime.notEquals=" + UPDATED_STARTTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByStarttimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where starttime in DEFAULT_STARTTIME or UPDATED_STARTTIME
        defaultEventShouldBeFound("starttime.in=" + DEFAULT_STARTTIME + "," + UPDATED_STARTTIME);

        // Get all the eventList where starttime equals to UPDATED_STARTTIME
        defaultEventShouldNotBeFound("starttime.in=" + UPDATED_STARTTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByStarttimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where starttime is not null
        defaultEventShouldBeFound("starttime.specified=true");

        // Get all the eventList where starttime is null
        defaultEventShouldNotBeFound("starttime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate equals to DEFAULT_STARTDATE
        defaultEventShouldBeFound("startdate.equals=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate equals to UPDATED_STARTDATE
        defaultEventShouldNotBeFound("startdate.equals=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate not equals to DEFAULT_STARTDATE
        defaultEventShouldNotBeFound("startdate.notEquals=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate not equals to UPDATED_STARTDATE
        defaultEventShouldBeFound("startdate.notEquals=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate in DEFAULT_STARTDATE or UPDATED_STARTDATE
        defaultEventShouldBeFound("startdate.in=" + DEFAULT_STARTDATE + "," + UPDATED_STARTDATE);

        // Get all the eventList where startdate equals to UPDATED_STARTDATE
        defaultEventShouldNotBeFound("startdate.in=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate is not null
        defaultEventShouldBeFound("startdate.specified=true");

        // Get all the eventList where startdate is null
        defaultEventShouldNotBeFound("startdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate is greater than or equal to DEFAULT_STARTDATE
        defaultEventShouldBeFound("startdate.greaterThanOrEqual=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate is greater than or equal to UPDATED_STARTDATE
        defaultEventShouldNotBeFound("startdate.greaterThanOrEqual=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate is less than or equal to DEFAULT_STARTDATE
        defaultEventShouldBeFound("startdate.lessThanOrEqual=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate is less than or equal to SMALLER_STARTDATE
        defaultEventShouldNotBeFound("startdate.lessThanOrEqual=" + SMALLER_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate is less than DEFAULT_STARTDATE
        defaultEventShouldNotBeFound("startdate.lessThan=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate is less than UPDATED_STARTDATE
        defaultEventShouldBeFound("startdate.lessThan=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByStartdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where startdate is greater than DEFAULT_STARTDATE
        defaultEventShouldNotBeFound("startdate.greaterThan=" + DEFAULT_STARTDATE);

        // Get all the eventList where startdate is greater than SMALLER_STARTDATE
        defaultEventShouldBeFound("startdate.greaterThan=" + SMALLER_STARTDATE);
    }


    @Test
    @Transactional
    public void getAllEventsByEndtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endtime equals to DEFAULT_ENDTIME
        defaultEventShouldBeFound("endtime.equals=" + DEFAULT_ENDTIME);

        // Get all the eventList where endtime equals to UPDATED_ENDTIME
        defaultEventShouldNotBeFound("endtime.equals=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByEndtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endtime not equals to DEFAULT_ENDTIME
        defaultEventShouldNotBeFound("endtime.notEquals=" + DEFAULT_ENDTIME);

        // Get all the eventList where endtime not equals to UPDATED_ENDTIME
        defaultEventShouldBeFound("endtime.notEquals=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByEndtimeIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endtime in DEFAULT_ENDTIME or UPDATED_ENDTIME
        defaultEventShouldBeFound("endtime.in=" + DEFAULT_ENDTIME + "," + UPDATED_ENDTIME);

        // Get all the eventList where endtime equals to UPDATED_ENDTIME
        defaultEventShouldNotBeFound("endtime.in=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllEventsByEndtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endtime is not null
        defaultEventShouldBeFound("endtime.specified=true");

        // Get all the eventList where endtime is null
        defaultEventShouldNotBeFound("endtime.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate equals to DEFAULT_ENDDATE
        defaultEventShouldBeFound("enddate.equals=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate equals to UPDATED_ENDDATE
        defaultEventShouldNotBeFound("enddate.equals=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate not equals to DEFAULT_ENDDATE
        defaultEventShouldNotBeFound("enddate.notEquals=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate not equals to UPDATED_ENDDATE
        defaultEventShouldBeFound("enddate.notEquals=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate in DEFAULT_ENDDATE or UPDATED_ENDDATE
        defaultEventShouldBeFound("enddate.in=" + DEFAULT_ENDDATE + "," + UPDATED_ENDDATE);

        // Get all the eventList where enddate equals to UPDATED_ENDDATE
        defaultEventShouldNotBeFound("enddate.in=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate is not null
        defaultEventShouldBeFound("enddate.specified=true");

        // Get all the eventList where enddate is null
        defaultEventShouldNotBeFound("enddate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate is greater than or equal to DEFAULT_ENDDATE
        defaultEventShouldBeFound("enddate.greaterThanOrEqual=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate is greater than or equal to UPDATED_ENDDATE
        defaultEventShouldNotBeFound("enddate.greaterThanOrEqual=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate is less than or equal to DEFAULT_ENDDATE
        defaultEventShouldBeFound("enddate.lessThanOrEqual=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate is less than or equal to SMALLER_ENDDATE
        defaultEventShouldNotBeFound("enddate.lessThanOrEqual=" + SMALLER_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate is less than DEFAULT_ENDDATE
        defaultEventShouldNotBeFound("enddate.lessThan=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate is less than UPDATED_ENDDATE
        defaultEventShouldBeFound("enddate.lessThan=" + UPDATED_ENDDATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEnddateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where enddate is greater than DEFAULT_ENDDATE
        defaultEventShouldNotBeFound("enddate.greaterThan=" + DEFAULT_ENDDATE);

        // Get all the eventList where enddate is greater than SMALLER_ENDDATE
        defaultEventShouldBeFound("enddate.greaterThan=" + SMALLER_ENDDATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].Description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].starttime").value(hasItem(DEFAULT_STARTTIME.toString())))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(DEFAULT_STARTDATE.toString())))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME.toString())))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(DEFAULT_ENDDATE.toString())));

        // Check, that the count call also returns 1
        restEventMockMvc.perform(get("/api/events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc.perform(get("/api/events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .Description(UPDATED_DESCRIPTION)
            .starttime(UPDATED_STARTTIME)
            .startdate(UPDATED_STARTDATE)
            .endtime(UPDATED_ENDTIME)
            .enddate(UPDATED_ENDDATE);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvent.getStarttime()).isEqualTo(UPDATED_STARTTIME);
        assertThat(testEvent.getStartdate()).isEqualTo(UPDATED_STARTDATE);
        assertThat(testEvent.getEndtime()).isEqualTo(UPDATED_ENDTIME);
        assertThat(testEvent.getEnddate()).isEqualTo(UPDATED_ENDDATE);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).save(testEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(0)).save(event);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Event in Elasticsearch
        verify(mockEventSearchRepository, times(1)).deleteById(event.getId());
    }

    @Test
    @Transactional
    public void searchEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        when(mockEventSearchRepository.search(queryStringQuery("id:" + event.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(event), PageRequest.of(0, 1), 1));
        // Search the event
        restEventMockMvc.perform(get("/api/_search/events?query=id:" + event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].Description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].starttime").value(hasItem(DEFAULT_STARTTIME.toString())))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(DEFAULT_STARTDATE.toString())))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME.toString())))
            .andExpect(jsonPath("$.[*].enddate").value(hasItem(DEFAULT_ENDDATE.toString())));
    }
}
