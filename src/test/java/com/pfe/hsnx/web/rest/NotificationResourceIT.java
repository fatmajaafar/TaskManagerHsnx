package com.pfe.hsnx.web.rest;

import com.pfe.hsnx.TaskManagerHsnxApp;
import com.pfe.hsnx.domain.Notification;
import com.pfe.hsnx.repository.NotificationRepository;
import com.pfe.hsnx.repository.search.NotificationSearchRepository;
import com.pfe.hsnx.service.NotificationService;
import com.pfe.hsnx.service.dto.NotificationDTO;
import com.pfe.hsnx.service.mapper.NotificationMapper;
import com.pfe.hsnx.web.rest.errors.ExceptionTranslator;
import com.pfe.hsnx.service.dto.NotificationCriteria;
import com.pfe.hsnx.service.NotificationQueryService;

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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = TaskManagerHsnxApp.class)
public class NotificationResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HANDLED = false;
    private static final Boolean UPDATED_HANDLED = true;

    private static final String DEFAULT_NOTIF_FROM = "AAAAAAAAAA";
    private static final String UPDATED_NOTIF_FROM = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFTO = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFTO = "BBBBBBBBBB";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;

    /**
     * This repository is mocked in the com.pfe.hsnx.repository.search test package.
     *
     * @see com.pfe.hsnx.repository.search.NotificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private NotificationSearchRepository mockNotificationSearchRepository;

    @Autowired
    private NotificationQueryService notificationQueryService;

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

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotificationResource notificationResource = new NotificationResource(notificationService, notificationQueryService);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource)
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
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .message(DEFAULT_MESSAGE)
            .handled(DEFAULT_HANDLED)
            .notifFrom(DEFAULT_NOTIF_FROM)
            .notifto(DEFAULT_NOTIFTO);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .message(UPDATED_MESSAGE)
            .handled(UPDATED_HANDLED)
            .notifFrom(UPDATED_NOTIF_FROM)
            .notifto(UPDATED_NOTIFTO);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.isHandled()).isEqualTo(DEFAULT_HANDLED);
        assertThat(testNotification.getNotifFrom()).isEqualTo(DEFAULT_NOTIF_FROM);
        assertThat(testNotification.getNotifto()).isEqualTo(DEFAULT_NOTIFTO);

        // Validate the Notification in Elasticsearch
        verify(mockNotificationSearchRepository, times(1)).save(testNotification);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Notification in Elasticsearch
        verify(mockNotificationSearchRepository, times(0)).save(notification);
    }


    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setMessage(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.booleanValue())))
            .andExpect(jsonPath("$.[*].notifFrom").value(hasItem(DEFAULT_NOTIF_FROM)))
            .andExpect(jsonPath("$.[*].notifto").value(hasItem(DEFAULT_NOTIFTO)));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.handled").value(DEFAULT_HANDLED.booleanValue()))
            .andExpect(jsonPath("$.notifFrom").value(DEFAULT_NOTIF_FROM))
            .andExpect(jsonPath("$.notifto").value(DEFAULT_NOTIFTO));
    }


    @Test
    @Transactional
    public void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotificationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message equals to DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message not equals to DEFAULT_MESSAGE
        defaultNotificationShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message not equals to UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message is not null
        defaultNotificationShouldBeFound("message.specified=true");

        // Get all the notificationList where message is null
        defaultNotificationShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message contains DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message contains UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message does not contain DEFAULT_MESSAGE
        defaultNotificationShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message does not contain UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllNotificationsByHandledIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where handled equals to DEFAULT_HANDLED
        defaultNotificationShouldBeFound("handled.equals=" + DEFAULT_HANDLED);

        // Get all the notificationList where handled equals to UPDATED_HANDLED
        defaultNotificationShouldNotBeFound("handled.equals=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    public void getAllNotificationsByHandledIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where handled not equals to DEFAULT_HANDLED
        defaultNotificationShouldNotBeFound("handled.notEquals=" + DEFAULT_HANDLED);

        // Get all the notificationList where handled not equals to UPDATED_HANDLED
        defaultNotificationShouldBeFound("handled.notEquals=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    public void getAllNotificationsByHandledIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where handled in DEFAULT_HANDLED or UPDATED_HANDLED
        defaultNotificationShouldBeFound("handled.in=" + DEFAULT_HANDLED + "," + UPDATED_HANDLED);

        // Get all the notificationList where handled equals to UPDATED_HANDLED
        defaultNotificationShouldNotBeFound("handled.in=" + UPDATED_HANDLED);
    }

    @Test
    @Transactional
    public void getAllNotificationsByHandledIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where handled is not null
        defaultNotificationShouldBeFound("handled.specified=true");

        // Get all the notificationList where handled is null
        defaultNotificationShouldNotBeFound("handled.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotifFromIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom equals to DEFAULT_NOTIF_FROM
        defaultNotificationShouldBeFound("notifFrom.equals=" + DEFAULT_NOTIF_FROM);

        // Get all the notificationList where notifFrom equals to UPDATED_NOTIF_FROM
        defaultNotificationShouldNotBeFound("notifFrom.equals=" + UPDATED_NOTIF_FROM);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotifFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom not equals to DEFAULT_NOTIF_FROM
        defaultNotificationShouldNotBeFound("notifFrom.notEquals=" + DEFAULT_NOTIF_FROM);

        // Get all the notificationList where notifFrom not equals to UPDATED_NOTIF_FROM
        defaultNotificationShouldBeFound("notifFrom.notEquals=" + UPDATED_NOTIF_FROM);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotifFromIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom in DEFAULT_NOTIF_FROM or UPDATED_NOTIF_FROM
        defaultNotificationShouldBeFound("notifFrom.in=" + DEFAULT_NOTIF_FROM + "," + UPDATED_NOTIF_FROM);

        // Get all the notificationList where notifFrom equals to UPDATED_NOTIF_FROM
        defaultNotificationShouldNotBeFound("notifFrom.in=" + UPDATED_NOTIF_FROM);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotifFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom is not null
        defaultNotificationShouldBeFound("notifFrom.specified=true");

        // Get all the notificationList where notifFrom is null
        defaultNotificationShouldNotBeFound("notifFrom.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByNotifFromContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom contains DEFAULT_NOTIF_FROM
        defaultNotificationShouldBeFound("notifFrom.contains=" + DEFAULT_NOTIF_FROM);

        // Get all the notificationList where notifFrom contains UPDATED_NOTIF_FROM
        defaultNotificationShouldNotBeFound("notifFrom.contains=" + UPDATED_NOTIF_FROM);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotifFromNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifFrom does not contain DEFAULT_NOTIF_FROM
        defaultNotificationShouldNotBeFound("notifFrom.doesNotContain=" + DEFAULT_NOTIF_FROM);

        // Get all the notificationList where notifFrom does not contain UPDATED_NOTIF_FROM
        defaultNotificationShouldBeFound("notifFrom.doesNotContain=" + UPDATED_NOTIF_FROM);
    }


    @Test
    @Transactional
    public void getAllNotificationsByNotiftoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto equals to DEFAULT_NOTIFTO
        defaultNotificationShouldBeFound("notifto.equals=" + DEFAULT_NOTIFTO);

        // Get all the notificationList where notifto equals to UPDATED_NOTIFTO
        defaultNotificationShouldNotBeFound("notifto.equals=" + UPDATED_NOTIFTO);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotiftoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto not equals to DEFAULT_NOTIFTO
        defaultNotificationShouldNotBeFound("notifto.notEquals=" + DEFAULT_NOTIFTO);

        // Get all the notificationList where notifto not equals to UPDATED_NOTIFTO
        defaultNotificationShouldBeFound("notifto.notEquals=" + UPDATED_NOTIFTO);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotiftoIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto in DEFAULT_NOTIFTO or UPDATED_NOTIFTO
        defaultNotificationShouldBeFound("notifto.in=" + DEFAULT_NOTIFTO + "," + UPDATED_NOTIFTO);

        // Get all the notificationList where notifto equals to UPDATED_NOTIFTO
        defaultNotificationShouldNotBeFound("notifto.in=" + UPDATED_NOTIFTO);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotiftoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto is not null
        defaultNotificationShouldBeFound("notifto.specified=true");

        // Get all the notificationList where notifto is null
        defaultNotificationShouldNotBeFound("notifto.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByNotiftoContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto contains DEFAULT_NOTIFTO
        defaultNotificationShouldBeFound("notifto.contains=" + DEFAULT_NOTIFTO);

        // Get all the notificationList where notifto contains UPDATED_NOTIFTO
        defaultNotificationShouldNotBeFound("notifto.contains=" + UPDATED_NOTIFTO);
    }

    @Test
    @Transactional
    public void getAllNotificationsByNotiftoNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where notifto does not contain DEFAULT_NOTIFTO
        defaultNotificationShouldNotBeFound("notifto.doesNotContain=" + DEFAULT_NOTIFTO);

        // Get all the notificationList where notifto does not contain UPDATED_NOTIFTO
        defaultNotificationShouldBeFound("notifto.doesNotContain=" + UPDATED_NOTIFTO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.booleanValue())))
            .andExpect(jsonPath("$.[*].notifFrom").value(hasItem(DEFAULT_NOTIF_FROM)))
            .andExpect(jsonPath("$.[*].notifto").value(hasItem(DEFAULT_NOTIFTO)));

        // Check, that the count call also returns 1
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .message(UPDATED_MESSAGE)
            .handled(UPDATED_HANDLED)
            .notifFrom(UPDATED_NOTIF_FROM)
            .notifto(UPDATED_NOTIFTO);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.isHandled()).isEqualTo(UPDATED_HANDLED);
        assertThat(testNotification.getNotifFrom()).isEqualTo(UPDATED_NOTIF_FROM);
        assertThat(testNotification.getNotifto()).isEqualTo(UPDATED_NOTIFTO);

        // Validate the Notification in Elasticsearch
        verify(mockNotificationSearchRepository, times(1)).save(testNotification);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Notification in Elasticsearch
        verify(mockNotificationSearchRepository, times(0)).save(notification);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Notification in Elasticsearch
        verify(mockNotificationSearchRepository, times(1)).deleteById(notification.getId());
    }

    @Test
    @Transactional
    public void searchNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        when(mockNotificationSearchRepository.search(queryStringQuery("id:" + notification.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(notification), PageRequest.of(0, 1), 1));
        // Search the notification
        restNotificationMockMvc.perform(get("/api/_search/notifications?query=id:" + notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].handled").value(hasItem(DEFAULT_HANDLED.booleanValue())))
            .andExpect(jsonPath("$.[*].notifFrom").value(hasItem(DEFAULT_NOTIF_FROM)))
            .andExpect(jsonPath("$.[*].notifto").value(hasItem(DEFAULT_NOTIFTO)));
    }
}
