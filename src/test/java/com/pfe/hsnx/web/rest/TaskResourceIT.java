package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Task;
import com.pfe.hsnx.domain.Employee;
import com.pfe.hsnx.repository.TaskRepository;
import com.pfe.hsnx.repository.search.TaskSearchRepository;
import com.pfe.hsnx.service.TaskService;
import com.pfe.hsnx.service.dto.TaskDTO;
import com.pfe.hsnx.service.mapper.TaskMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.TaskCriteria;
import com.pfe.hsnx.service.TaskQueryService;

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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class TaskResourceIT {

    private static final String DEFAULT_TASKTITLE = "AAAAAAAAAA";
    private static final String UPDATED_TASKTITLE = "BBBBBBBBBB";

    private static final String DEFAULT_TASKDESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_TASKDESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_START = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_TIME_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_DATE_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_END = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_TIME_END = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_END = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TASKSTATUS = 1;
    private static final Integer UPDATED_TASKSTATUS = 2;
    private static final Integer SMALLER_TASKSTATUS = 1 - 1;

    private static final Integer DEFAULT_TASKPRIORITY = 1;
    private static final Integer UPDATED_TASKPRIORITY = 2;
    private static final Integer SMALLER_TASKPRIORITY = 1 - 1;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TASKCATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_TASKCATEGORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_TASKSTATE = 1;
    private static final Integer UPDATED_TASKSTATE = 2;
    private static final Integer SMALLER_TASKSTATE = 1 - 1;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.TaskSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaskSearchRepository mockTaskSearchRepository;

    @Autowired
    private TaskQueryService taskQueryService;

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

    private MockMvc restTaskMockMvc;

    private Task task;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskResource taskResource = new TaskResource(taskService, taskQueryService);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
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
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .tasktitle(DEFAULT_TASKTITLE)
            .taskdescription(DEFAULT_TASKDESCRIPTION)
            .dateStart(DEFAULT_DATE_START)
            .timeStart(DEFAULT_TIME_START)
            .dateEnd(DEFAULT_DATE_END)
            .timeEnd(DEFAULT_TIME_END)
            .taskstatus(DEFAULT_TASKSTATUS)
            .taskpriority(DEFAULT_TASKPRIORITY)
            .dueDate(DEFAULT_DUE_DATE)
            .taskcategory(DEFAULT_TASKCATEGORY)
            .taskstate(DEFAULT_TASKSTATE);
        return task;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .tasktitle(UPDATED_TASKTITLE)
            .taskdescription(UPDATED_TASKDESCRIPTION)
            .dateStart(UPDATED_DATE_START)
            .timeStart(UPDATED_TIME_START)
            .dateEnd(UPDATED_DATE_END)
            .timeEnd(UPDATED_TIME_END)
            .taskstatus(UPDATED_TASKSTATUS)
            .taskpriority(UPDATED_TASKPRIORITY)
            .dueDate(UPDATED_DUE_DATE)
            .taskcategory(UPDATED_TASKCATEGORY)
            .taskstate(UPDATED_TASKSTATE);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTasktitle()).isEqualTo(DEFAULT_TASKTITLE);
        assertThat(testTask.getTaskdescription()).isEqualTo(DEFAULT_TASKDESCRIPTION);
        assertThat(testTask.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testTask.getTimeStart()).isEqualTo(DEFAULT_TIME_START);
        assertThat(testTask.getDateEnd()).isEqualTo(DEFAULT_DATE_END);
        assertThat(testTask.getTimeEnd()).isEqualTo(DEFAULT_TIME_END);
        assertThat(testTask.getTaskstatus()).isEqualTo(DEFAULT_TASKSTATUS);
        assertThat(testTask.getTaskpriority()).isEqualTo(DEFAULT_TASKPRIORITY);
        assertThat(testTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testTask.getTaskcategory()).isEqualTo(DEFAULT_TASKCATEGORY);
        assertThat(testTask.getTaskstate()).isEqualTo(DEFAULT_TASKSTATE);

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).save(testTask);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task with an existing ID
        task.setId(1L);
        TaskDTO taskDTO = taskMapper.toDto(task);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task);
    }


    @Test
    @Transactional
    public void checkTasktitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = taskRepository.findAll().size();
        // set the field null
        task.setTasktitle(null);

        // Create the Task, which fails.
        TaskDTO taskDTO = taskMapper.toDto(task);

        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].tasktitle").value(hasItem(DEFAULT_TASKTITLE)))
            .andExpect(jsonPath("$.[*].taskdescription").value(hasItem(DEFAULT_TASKDESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END.toString())))
            .andExpect(jsonPath("$.[*].taskstatus").value(hasItem(DEFAULT_TASKSTATUS)))
            .andExpect(jsonPath("$.[*].taskpriority").value(hasItem(DEFAULT_TASKPRIORITY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taskcategory").value(hasItem(DEFAULT_TASKCATEGORY)))
            .andExpect(jsonPath("$.[*].taskstate").value(hasItem(DEFAULT_TASKSTATE)));
    }
    
    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.tasktitle").value(DEFAULT_TASKTITLE))
            .andExpect(jsonPath("$.taskdescription").value(DEFAULT_TASKDESCRIPTION))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.timeStart").value(DEFAULT_TIME_START.toString()))
            .andExpect(jsonPath("$.dateEnd").value(DEFAULT_DATE_END.toString()))
            .andExpect(jsonPath("$.timeEnd").value(DEFAULT_TIME_END.toString()))
            .andExpect(jsonPath("$.taskstatus").value(DEFAULT_TASKSTATUS))
            .andExpect(jsonPath("$.taskpriority").value(DEFAULT_TASKPRIORITY))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.taskcategory").value(DEFAULT_TASKCATEGORY))
            .andExpect(jsonPath("$.taskstate").value(DEFAULT_TASKSTATE));
    }


    @Test
    @Transactional
    public void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTasksByTasktitleIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle equals to DEFAULT_TASKTITLE
        defaultTaskShouldBeFound("tasktitle.equals=" + DEFAULT_TASKTITLE);

        // Get all the taskList where tasktitle equals to UPDATED_TASKTITLE
        defaultTaskShouldNotBeFound("tasktitle.equals=" + UPDATED_TASKTITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTasktitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle not equals to DEFAULT_TASKTITLE
        defaultTaskShouldNotBeFound("tasktitle.notEquals=" + DEFAULT_TASKTITLE);

        // Get all the taskList where tasktitle not equals to UPDATED_TASKTITLE
        defaultTaskShouldBeFound("tasktitle.notEquals=" + UPDATED_TASKTITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTasktitleIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle in DEFAULT_TASKTITLE or UPDATED_TASKTITLE
        defaultTaskShouldBeFound("tasktitle.in=" + DEFAULT_TASKTITLE + "," + UPDATED_TASKTITLE);

        // Get all the taskList where tasktitle equals to UPDATED_TASKTITLE
        defaultTaskShouldNotBeFound("tasktitle.in=" + UPDATED_TASKTITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTasktitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle is not null
        defaultTaskShouldBeFound("tasktitle.specified=true");

        // Get all the taskList where tasktitle is null
        defaultTaskShouldNotBeFound("tasktitle.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByTasktitleContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle contains DEFAULT_TASKTITLE
        defaultTaskShouldBeFound("tasktitle.contains=" + DEFAULT_TASKTITLE);

        // Get all the taskList where tasktitle contains UPDATED_TASKTITLE
        defaultTaskShouldNotBeFound("tasktitle.contains=" + UPDATED_TASKTITLE);
    }

    @Test
    @Transactional
    public void getAllTasksByTasktitleNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where tasktitle does not contain DEFAULT_TASKTITLE
        defaultTaskShouldNotBeFound("tasktitle.doesNotContain=" + DEFAULT_TASKTITLE);

        // Get all the taskList where tasktitle does not contain UPDATED_TASKTITLE
        defaultTaskShouldBeFound("tasktitle.doesNotContain=" + UPDATED_TASKTITLE);
    }


    @Test
    @Transactional
    public void getAllTasksByTaskdescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription equals to DEFAULT_TASKDESCRIPTION
        defaultTaskShouldBeFound("taskdescription.equals=" + DEFAULT_TASKDESCRIPTION);

        // Get all the taskList where taskdescription equals to UPDATED_TASKDESCRIPTION
        defaultTaskShouldNotBeFound("taskdescription.equals=" + UPDATED_TASKDESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskdescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription not equals to DEFAULT_TASKDESCRIPTION
        defaultTaskShouldNotBeFound("taskdescription.notEquals=" + DEFAULT_TASKDESCRIPTION);

        // Get all the taskList where taskdescription not equals to UPDATED_TASKDESCRIPTION
        defaultTaskShouldBeFound("taskdescription.notEquals=" + UPDATED_TASKDESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskdescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription in DEFAULT_TASKDESCRIPTION or UPDATED_TASKDESCRIPTION
        defaultTaskShouldBeFound("taskdescription.in=" + DEFAULT_TASKDESCRIPTION + "," + UPDATED_TASKDESCRIPTION);

        // Get all the taskList where taskdescription equals to UPDATED_TASKDESCRIPTION
        defaultTaskShouldNotBeFound("taskdescription.in=" + UPDATED_TASKDESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskdescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription is not null
        defaultTaskShouldBeFound("taskdescription.specified=true");

        // Get all the taskList where taskdescription is null
        defaultTaskShouldNotBeFound("taskdescription.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByTaskdescriptionContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription contains DEFAULT_TASKDESCRIPTION
        defaultTaskShouldBeFound("taskdescription.contains=" + DEFAULT_TASKDESCRIPTION);

        // Get all the taskList where taskdescription contains UPDATED_TASKDESCRIPTION
        defaultTaskShouldNotBeFound("taskdescription.contains=" + UPDATED_TASKDESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskdescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskdescription does not contain DEFAULT_TASKDESCRIPTION
        defaultTaskShouldNotBeFound("taskdescription.doesNotContain=" + DEFAULT_TASKDESCRIPTION);

        // Get all the taskList where taskdescription does not contain UPDATED_TASKDESCRIPTION
        defaultTaskShouldBeFound("taskdescription.doesNotContain=" + UPDATED_TASKDESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllTasksByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart equals to DEFAULT_DATE_START
        defaultTaskShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart equals to UPDATED_DATE_START
        defaultTaskShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart not equals to DEFAULT_DATE_START
        defaultTaskShouldNotBeFound("dateStart.notEquals=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart not equals to UPDATED_DATE_START
        defaultTaskShouldBeFound("dateStart.notEquals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultTaskShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the taskList where dateStart equals to UPDATED_DATE_START
        defaultTaskShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart is not null
        defaultTaskShouldBeFound("dateStart.specified=true");

        // Get all the taskList where dateStart is null
        defaultTaskShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart is greater than or equal to DEFAULT_DATE_START
        defaultTaskShouldBeFound("dateStart.greaterThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart is greater than or equal to UPDATED_DATE_START
        defaultTaskShouldNotBeFound("dateStart.greaterThanOrEqual=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart is less than or equal to DEFAULT_DATE_START
        defaultTaskShouldBeFound("dateStart.lessThanOrEqual=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart is less than or equal to SMALLER_DATE_START
        defaultTaskShouldNotBeFound("dateStart.lessThanOrEqual=" + SMALLER_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart is less than DEFAULT_DATE_START
        defaultTaskShouldNotBeFound("dateStart.lessThan=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart is less than UPDATED_DATE_START
        defaultTaskShouldBeFound("dateStart.lessThan=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    public void getAllTasksByDateStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateStart is greater than DEFAULT_DATE_START
        defaultTaskShouldNotBeFound("dateStart.greaterThan=" + DEFAULT_DATE_START);

        // Get all the taskList where dateStart is greater than SMALLER_DATE_START
        defaultTaskShouldBeFound("dateStart.greaterThan=" + SMALLER_DATE_START);
    }


    @Test
    @Transactional
    public void getAllTasksByTimeStartIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeStart equals to DEFAULT_TIME_START
        defaultTaskShouldBeFound("timeStart.equals=" + DEFAULT_TIME_START);

        // Get all the taskList where timeStart equals to UPDATED_TIME_START
        defaultTaskShouldNotBeFound("timeStart.equals=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeStart not equals to DEFAULT_TIME_START
        defaultTaskShouldNotBeFound("timeStart.notEquals=" + DEFAULT_TIME_START);

        // Get all the taskList where timeStart not equals to UPDATED_TIME_START
        defaultTaskShouldBeFound("timeStart.notEquals=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeStartIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeStart in DEFAULT_TIME_START or UPDATED_TIME_START
        defaultTaskShouldBeFound("timeStart.in=" + DEFAULT_TIME_START + "," + UPDATED_TIME_START);

        // Get all the taskList where timeStart equals to UPDATED_TIME_START
        defaultTaskShouldNotBeFound("timeStart.in=" + UPDATED_TIME_START);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeStart is not null
        defaultTaskShouldBeFound("timeStart.specified=true");

        // Get all the taskList where timeStart is null
        defaultTaskShouldNotBeFound("timeStart.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd equals to DEFAULT_DATE_END
        defaultTaskShouldBeFound("dateEnd.equals=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd equals to UPDATED_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.equals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd not equals to DEFAULT_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.notEquals=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd not equals to UPDATED_DATE_END
        defaultTaskShouldBeFound("dateEnd.notEquals=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd in DEFAULT_DATE_END or UPDATED_DATE_END
        defaultTaskShouldBeFound("dateEnd.in=" + DEFAULT_DATE_END + "," + UPDATED_DATE_END);

        // Get all the taskList where dateEnd equals to UPDATED_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.in=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd is not null
        defaultTaskShouldBeFound("dateEnd.specified=true");

        // Get all the taskList where dateEnd is null
        defaultTaskShouldNotBeFound("dateEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd is greater than or equal to DEFAULT_DATE_END
        defaultTaskShouldBeFound("dateEnd.greaterThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd is greater than or equal to UPDATED_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.greaterThanOrEqual=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd is less than or equal to DEFAULT_DATE_END
        defaultTaskShouldBeFound("dateEnd.lessThanOrEqual=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd is less than or equal to SMALLER_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.lessThanOrEqual=" + SMALLER_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd is less than DEFAULT_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.lessThan=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd is less than UPDATED_DATE_END
        defaultTaskShouldBeFound("dateEnd.lessThan=" + UPDATED_DATE_END);
    }

    @Test
    @Transactional
    public void getAllTasksByDateEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dateEnd is greater than DEFAULT_DATE_END
        defaultTaskShouldNotBeFound("dateEnd.greaterThan=" + DEFAULT_DATE_END);

        // Get all the taskList where dateEnd is greater than SMALLER_DATE_END
        defaultTaskShouldBeFound("dateEnd.greaterThan=" + SMALLER_DATE_END);
    }


    @Test
    @Transactional
    public void getAllTasksByTimeEndIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeEnd equals to DEFAULT_TIME_END
        defaultTaskShouldBeFound("timeEnd.equals=" + DEFAULT_TIME_END);

        // Get all the taskList where timeEnd equals to UPDATED_TIME_END
        defaultTaskShouldNotBeFound("timeEnd.equals=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeEndIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeEnd not equals to DEFAULT_TIME_END
        defaultTaskShouldNotBeFound("timeEnd.notEquals=" + DEFAULT_TIME_END);

        // Get all the taskList where timeEnd not equals to UPDATED_TIME_END
        defaultTaskShouldBeFound("timeEnd.notEquals=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeEndIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeEnd in DEFAULT_TIME_END or UPDATED_TIME_END
        defaultTaskShouldBeFound("timeEnd.in=" + DEFAULT_TIME_END + "," + UPDATED_TIME_END);

        // Get all the taskList where timeEnd equals to UPDATED_TIME_END
        defaultTaskShouldNotBeFound("timeEnd.in=" + UPDATED_TIME_END);
    }

    @Test
    @Transactional
    public void getAllTasksByTimeEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where timeEnd is not null
        defaultTaskShouldBeFound("timeEnd.specified=true");

        // Get all the taskList where timeEnd is null
        defaultTaskShouldNotBeFound("timeEnd.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus equals to DEFAULT_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.equals=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus equals to UPDATED_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.equals=" + UPDATED_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus not equals to DEFAULT_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.notEquals=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus not equals to UPDATED_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.notEquals=" + UPDATED_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus in DEFAULT_TASKSTATUS or UPDATED_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.in=" + DEFAULT_TASKSTATUS + "," + UPDATED_TASKSTATUS);

        // Get all the taskList where taskstatus equals to UPDATED_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.in=" + UPDATED_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus is not null
        defaultTaskShouldBeFound("taskstatus.specified=true");

        // Get all the taskList where taskstatus is null
        defaultTaskShouldNotBeFound("taskstatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus is greater than or equal to DEFAULT_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.greaterThanOrEqual=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus is greater than or equal to UPDATED_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.greaterThanOrEqual=" + UPDATED_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus is less than or equal to DEFAULT_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.lessThanOrEqual=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus is less than or equal to SMALLER_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.lessThanOrEqual=" + SMALLER_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus is less than DEFAULT_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.lessThan=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus is less than UPDATED_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.lessThan=" + UPDATED_TASKSTATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstatus is greater than DEFAULT_TASKSTATUS
        defaultTaskShouldNotBeFound("taskstatus.greaterThan=" + DEFAULT_TASKSTATUS);

        // Get all the taskList where taskstatus is greater than SMALLER_TASKSTATUS
        defaultTaskShouldBeFound("taskstatus.greaterThan=" + SMALLER_TASKSTATUS);
    }


    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority equals to DEFAULT_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.equals=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority equals to UPDATED_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.equals=" + UPDATED_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority not equals to DEFAULT_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.notEquals=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority not equals to UPDATED_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.notEquals=" + UPDATED_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority in DEFAULT_TASKPRIORITY or UPDATED_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.in=" + DEFAULT_TASKPRIORITY + "," + UPDATED_TASKPRIORITY);

        // Get all the taskList where taskpriority equals to UPDATED_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.in=" + UPDATED_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority is not null
        defaultTaskShouldBeFound("taskpriority.specified=true");

        // Get all the taskList where taskpriority is null
        defaultTaskShouldNotBeFound("taskpriority.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority is greater than or equal to DEFAULT_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.greaterThanOrEqual=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority is greater than or equal to UPDATED_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.greaterThanOrEqual=" + UPDATED_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority is less than or equal to DEFAULT_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.lessThanOrEqual=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority is less than or equal to SMALLER_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.lessThanOrEqual=" + SMALLER_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority is less than DEFAULT_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.lessThan=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority is less than UPDATED_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.lessThan=" + UPDATED_TASKPRIORITY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskpriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskpriority is greater than DEFAULT_TASKPRIORITY
        defaultTaskShouldNotBeFound("taskpriority.greaterThan=" + DEFAULT_TASKPRIORITY);

        // Get all the taskList where taskpriority is greater than SMALLER_TASKPRIORITY
        defaultTaskShouldBeFound("taskpriority.greaterThan=" + SMALLER_TASKPRIORITY);
    }


    @Test
    @Transactional
    public void getAllTasksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate equals to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate not equals to DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate not equals to UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is not null
        defaultTaskShouldBeFound("dueDate.specified=true");

        // Get all the taskList where dueDate is null
        defaultTaskShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is less than DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is less than UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is greater than DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate is greater than SMALLER_DUE_DATE
        defaultTaskShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }


    @Test
    @Transactional
    public void getAllTasksByTaskcategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory equals to DEFAULT_TASKCATEGORY
        defaultTaskShouldBeFound("taskcategory.equals=" + DEFAULT_TASKCATEGORY);

        // Get all the taskList where taskcategory equals to UPDATED_TASKCATEGORY
        defaultTaskShouldNotBeFound("taskcategory.equals=" + UPDATED_TASKCATEGORY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskcategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory not equals to DEFAULT_TASKCATEGORY
        defaultTaskShouldNotBeFound("taskcategory.notEquals=" + DEFAULT_TASKCATEGORY);

        // Get all the taskList where taskcategory not equals to UPDATED_TASKCATEGORY
        defaultTaskShouldBeFound("taskcategory.notEquals=" + UPDATED_TASKCATEGORY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskcategoryIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory in DEFAULT_TASKCATEGORY or UPDATED_TASKCATEGORY
        defaultTaskShouldBeFound("taskcategory.in=" + DEFAULT_TASKCATEGORY + "," + UPDATED_TASKCATEGORY);

        // Get all the taskList where taskcategory equals to UPDATED_TASKCATEGORY
        defaultTaskShouldNotBeFound("taskcategory.in=" + UPDATED_TASKCATEGORY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskcategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory is not null
        defaultTaskShouldBeFound("taskcategory.specified=true");

        // Get all the taskList where taskcategory is null
        defaultTaskShouldNotBeFound("taskcategory.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByTaskcategoryContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory contains DEFAULT_TASKCATEGORY
        defaultTaskShouldBeFound("taskcategory.contains=" + DEFAULT_TASKCATEGORY);

        // Get all the taskList where taskcategory contains UPDATED_TASKCATEGORY
        defaultTaskShouldNotBeFound("taskcategory.contains=" + UPDATED_TASKCATEGORY);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskcategoryNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskcategory does not contain DEFAULT_TASKCATEGORY
        defaultTaskShouldNotBeFound("taskcategory.doesNotContain=" + DEFAULT_TASKCATEGORY);

        // Get all the taskList where taskcategory does not contain UPDATED_TASKCATEGORY
        defaultTaskShouldBeFound("taskcategory.doesNotContain=" + UPDATED_TASKCATEGORY);
    }


    @Test
    @Transactional
    public void getAllTasksByTaskstateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate equals to DEFAULT_TASKSTATE
        defaultTaskShouldBeFound("taskstate.equals=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate equals to UPDATED_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.equals=" + UPDATED_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate not equals to DEFAULT_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.notEquals=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate not equals to UPDATED_TASKSTATE
        defaultTaskShouldBeFound("taskstate.notEquals=" + UPDATED_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate in DEFAULT_TASKSTATE or UPDATED_TASKSTATE
        defaultTaskShouldBeFound("taskstate.in=" + DEFAULT_TASKSTATE + "," + UPDATED_TASKSTATE);

        // Get all the taskList where taskstate equals to UPDATED_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.in=" + UPDATED_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate is not null
        defaultTaskShouldBeFound("taskstate.specified=true");

        // Get all the taskList where taskstate is null
        defaultTaskShouldNotBeFound("taskstate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate is greater than or equal to DEFAULT_TASKSTATE
        defaultTaskShouldBeFound("taskstate.greaterThanOrEqual=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate is greater than or equal to UPDATED_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.greaterThanOrEqual=" + UPDATED_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate is less than or equal to DEFAULT_TASKSTATE
        defaultTaskShouldBeFound("taskstate.lessThanOrEqual=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate is less than or equal to SMALLER_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.lessThanOrEqual=" + SMALLER_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate is less than DEFAULT_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.lessThan=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate is less than UPDATED_TASKSTATE
        defaultTaskShouldBeFound("taskstate.lessThan=" + UPDATED_TASKSTATE);
    }

    @Test
    @Transactional
    public void getAllTasksByTaskstateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where taskstate is greater than DEFAULT_TASKSTATE
        defaultTaskShouldNotBeFound("taskstate.greaterThan=" + DEFAULT_TASKSTATE);

        // Get all the taskList where taskstate is greater than SMALLER_TASKSTATE
        defaultTaskShouldBeFound("taskstate.greaterThan=" + SMALLER_TASKSTATE);
    }


    @Test
    @Transactional
    public void getAllTasksByTblEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        Employee tblEmployee = EmployeeResourceIT.createEntity(em);
        em.persist(tblEmployee);
        em.flush();
        task.setTblEmployee(tblEmployee);
        taskRepository.saveAndFlush(task);
        Long tblEmployeeId = tblEmployee.getId();

        // Get all the taskList where tblEmployee equals to tblEmployeeId
        defaultTaskShouldBeFound("tblEmployeeId.equals=" + tblEmployeeId);

        // Get all the taskList where tblEmployee equals to tblEmployeeId + 1
        defaultTaskShouldNotBeFound("tblEmployeeId.equals=" + (tblEmployeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].tasktitle").value(hasItem(DEFAULT_TASKTITLE)))
            .andExpect(jsonPath("$.[*].taskdescription").value(hasItem(DEFAULT_TASKDESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END.toString())))
            .andExpect(jsonPath("$.[*].taskstatus").value(hasItem(DEFAULT_TASKSTATUS)))
            .andExpect(jsonPath("$.[*].taskpriority").value(hasItem(DEFAULT_TASKPRIORITY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taskcategory").value(hasItem(DEFAULT_TASKCATEGORY)))
            .andExpect(jsonPath("$.[*].taskstate").value(hasItem(DEFAULT_TASKSTATE)));

        // Check, that the count call also returns 1
        restTaskMockMvc.perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc.perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .tasktitle(UPDATED_TASKTITLE)
            .taskdescription(UPDATED_TASKDESCRIPTION)
            .dateStart(UPDATED_DATE_START)
            .timeStart(UPDATED_TIME_START)
            .dateEnd(UPDATED_DATE_END)
            .timeEnd(UPDATED_TIME_END)
            .taskstatus(UPDATED_TASKSTATUS)
            .taskpriority(UPDATED_TASKPRIORITY)
            .dueDate(UPDATED_DUE_DATE)
            .taskcategory(UPDATED_TASKCATEGORY)
            .taskstate(UPDATED_TASKSTATE);
        TaskDTO taskDTO = taskMapper.toDto(updatedTask);

        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getTasktitle()).isEqualTo(UPDATED_TASKTITLE);
        assertThat(testTask.getTaskdescription()).isEqualTo(UPDATED_TASKDESCRIPTION);
        assertThat(testTask.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testTask.getTimeStart()).isEqualTo(UPDATED_TIME_START);
        assertThat(testTask.getDateEnd()).isEqualTo(UPDATED_DATE_END);
        assertThat(testTask.getTimeEnd()).isEqualTo(UPDATED_TIME_END);
        assertThat(testTask.getTaskstatus()).isEqualTo(UPDATED_TASKSTATUS);
        assertThat(testTask.getTaskpriority()).isEqualTo(UPDATED_TASKPRIORITY);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTask.getTaskcategory()).isEqualTo(UPDATED_TASKCATEGORY);
        assertThat(testTask.getTaskstate()).isEqualTo(UPDATED_TASKSTATE);

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).save(testTask);
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Create the Task
        TaskDTO taskDTO = taskMapper.toDto(task);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(0)).save(task);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Task in Elasticsearch
        verify(mockTaskSearchRepository, times(1)).deleteById(task.getId());
    }

    @Test
    @Transactional
    public void searchTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        when(mockTaskSearchRepository.search(queryStringQuery("id:" + task.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(task), PageRequest.of(0, 1), 1));
        // Search the task
        restTaskMockMvc.perform(get("/api/_search/tasks?query=id:" + task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].tasktitle").value(hasItem(DEFAULT_TASKTITLE)))
            .andExpect(jsonPath("$.[*].taskdescription").value(hasItem(DEFAULT_TASKDESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].timeStart").value(hasItem(DEFAULT_TIME_START.toString())))
            .andExpect(jsonPath("$.[*].dateEnd").value(hasItem(DEFAULT_DATE_END.toString())))
            .andExpect(jsonPath("$.[*].timeEnd").value(hasItem(DEFAULT_TIME_END.toString())))
            .andExpect(jsonPath("$.[*].taskstatus").value(hasItem(DEFAULT_TASKSTATUS)))
            .andExpect(jsonPath("$.[*].taskpriority").value(hasItem(DEFAULT_TASKPRIORITY)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].taskcategory").value(hasItem(DEFAULT_TASKCATEGORY)))
            .andExpect(jsonPath("$.[*].taskstate").value(hasItem(DEFAULT_TASKSTATE)));
    }
}
