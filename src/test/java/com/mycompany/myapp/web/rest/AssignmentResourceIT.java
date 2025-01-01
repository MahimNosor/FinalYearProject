package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AssignmentAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.domain.enumeration.ProgrammingLanguage;
import com.mycompany.myapp.domain.enumeration.difficulty;
import com.mycompany.myapp.repository.AssignmentRepository;
import com.mycompany.myapp.repository.search.AssignmentSearchRepository;
import com.mycompany.myapp.service.AssignmentService;
import com.mycompany.myapp.service.dto.AssignmentDTO;
import com.mycompany.myapp.service.mapper.AssignmentMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AssignmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssignmentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final difficulty DEFAULT_DIFFICULTY = difficulty.BEGINNER;
    private static final difficulty UPDATED_DIFFICULTY = difficulty.MEDIUM;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ProgrammingLanguage DEFAULT_LANGUAGE = ProgrammingLanguage.JAVA;
    private static final ProgrammingLanguage UPDATED_LANGUAGE = ProgrammingLanguage.PYTHON;

    private static final String DEFAULT_TEST_CASES = "AAAAAAAAAA";
    private static final String UPDATED_TEST_CASES = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_SCORE = 1;
    private static final Integer UPDATED_MAX_SCORE = 2;
    private static final Integer SMALLER_MAX_SCORE = 1 - 1;

    private static final Boolean DEFAULT_IS_PRELOADED = false;
    private static final Boolean UPDATED_IS_PRELOADED = true;

    private static final String ENTITY_API_URL = "/api/assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/assignments/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Mock
    private AssignmentRepository assignmentRepositoryMock;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Mock
    private AssignmentService assignmentServiceMock;

    @Autowired
    private AssignmentSearchRepository assignmentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssignmentMockMvc;

    private Assignment assignment;

    private Assignment insertedAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createEntity(EntityManager em) {
        Assignment assignment = new Assignment()
            .title(DEFAULT_TITLE)
            .difficulty(DEFAULT_DIFFICULTY)
            .description(DEFAULT_DESCRIPTION)
            .language(DEFAULT_LANGUAGE)
            .testCases(DEFAULT_TEST_CASES)
            .maxScore(DEFAULT_MAX_SCORE)
            .isPreloaded(DEFAULT_IS_PRELOADED);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        assignment.setAppUser(appUser);
        // Add required entity
        StudentClass studentClass;
        if (TestUtil.findAll(em, StudentClass.class).isEmpty()) {
            studentClass = StudentClassResourceIT.createEntity();
            em.persist(studentClass);
            em.flush();
        } else {
            studentClass = TestUtil.findAll(em, StudentClass.class).get(0);
        }
        assignment.getStudentClasses().add(studentClass);
        return assignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assignment createUpdatedEntity(EntityManager em) {
        Assignment updatedAssignment = new Assignment()
            .title(UPDATED_TITLE)
            .difficulty(UPDATED_DIFFICULTY)
            .description(UPDATED_DESCRIPTION)
            .language(UPDATED_LANGUAGE)
            .testCases(UPDATED_TEST_CASES)
            .maxScore(UPDATED_MAX_SCORE)
            .isPreloaded(UPDATED_IS_PRELOADED);
        // Add required entity
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            appUser = AppUserResourceIT.createUpdatedEntity();
            em.persist(appUser);
            em.flush();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        updatedAssignment.setAppUser(appUser);
        // Add required entity
        StudentClass studentClass;
        if (TestUtil.findAll(em, StudentClass.class).isEmpty()) {
            studentClass = StudentClassResourceIT.createUpdatedEntity();
            em.persist(studentClass);
            em.flush();
        } else {
            studentClass = TestUtil.findAll(em, StudentClass.class).get(0);
        }
        updatedAssignment.getStudentClasses().add(studentClass);
        return updatedAssignment;
    }

    @BeforeEach
    public void initTest() {
        assignment = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAssignment != null) {
            assignmentRepository.delete(insertedAssignment);
            assignmentSearchRepository.delete(insertedAssignment);
            insertedAssignment = null;
        }
    }

    @Test
    @Transactional
    void createAssignment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);
        var returnedAssignmentDTO = om.readValue(
            restAssignmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssignmentDTO.class
        );

        // Validate the Assignment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssignment = assignmentMapper.toEntity(returnedAssignmentDTO);
        assertAssignmentUpdatableFieldsEquals(returnedAssignment, getPersistedAssignment(returnedAssignment));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAssignment = returnedAssignment;
    }

    @Test
    @Transactional
    void createAssignmentWithExistingId() throws Exception {
        // Create the Assignment with an existing ID
        assignment.setId(1L);
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        // set the field null
        assignment.setTitle(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDifficultyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        // set the field null
        assignment.setDifficulty(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        // set the field null
        assignment.setLanguage(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMaxScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        // set the field null
        assignment.setMaxScore(null);

        // Create the Assignment, which fails.
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        restAssignmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAssignments() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].testCases").value(hasItem(DEFAULT_TEST_CASES.toString())))
            .andExpect(jsonPath("$.[*].maxScore").value(hasItem(DEFAULT_MAX_SCORE)))
            .andExpect(jsonPath("$.[*].isPreloaded").value(hasItem(DEFAULT_IS_PRELOADED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssignmentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(assignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assignmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssignmentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assignmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssignmentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assignmentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssignment() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get the assignment
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL_ID, assignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assignment.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.testCases").value(DEFAULT_TEST_CASES.toString()))
            .andExpect(jsonPath("$.maxScore").value(DEFAULT_MAX_SCORE))
            .andExpect(jsonPath("$.isPreloaded").value(DEFAULT_IS_PRELOADED.booleanValue()));
    }

    @Test
    @Transactional
    void getAssignmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        Long id = assignment.getId();

        defaultAssignmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAssignmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAssignmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssignmentsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title equals to
        defaultAssignmentFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title in
        defaultAssignmentFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title is not null
        defaultAssignmentFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title contains
        defaultAssignmentFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where title does not contain
        defaultAssignmentFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByDifficultyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where difficulty equals to
        defaultAssignmentFiltering("difficulty.equals=" + DEFAULT_DIFFICULTY, "difficulty.equals=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void getAllAssignmentsByDifficultyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where difficulty in
        defaultAssignmentFiltering("difficulty.in=" + DEFAULT_DIFFICULTY + "," + UPDATED_DIFFICULTY, "difficulty.in=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void getAllAssignmentsByDifficultyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where difficulty is not null
        defaultAssignmentFiltering("difficulty.specified=true", "difficulty.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where language equals to
        defaultAssignmentFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where language in
        defaultAssignmentFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where language is not null
        defaultAssignmentFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore equals to
        defaultAssignmentFiltering("maxScore.equals=" + DEFAULT_MAX_SCORE, "maxScore.equals=" + UPDATED_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore in
        defaultAssignmentFiltering("maxScore.in=" + DEFAULT_MAX_SCORE + "," + UPDATED_MAX_SCORE, "maxScore.in=" + UPDATED_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore is not null
        defaultAssignmentFiltering("maxScore.specified=true", "maxScore.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore is greater than or equal to
        defaultAssignmentFiltering("maxScore.greaterThanOrEqual=" + DEFAULT_MAX_SCORE, "maxScore.greaterThanOrEqual=" + UPDATED_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore is less than or equal to
        defaultAssignmentFiltering("maxScore.lessThanOrEqual=" + DEFAULT_MAX_SCORE, "maxScore.lessThanOrEqual=" + SMALLER_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore is less than
        defaultAssignmentFiltering("maxScore.lessThan=" + UPDATED_MAX_SCORE, "maxScore.lessThan=" + DEFAULT_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByMaxScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where maxScore is greater than
        defaultAssignmentFiltering("maxScore.greaterThan=" + SMALLER_MAX_SCORE, "maxScore.greaterThan=" + DEFAULT_MAX_SCORE);
    }

    @Test
    @Transactional
    void getAllAssignmentsByIsPreloadedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where isPreloaded equals to
        defaultAssignmentFiltering("isPreloaded.equals=" + DEFAULT_IS_PRELOADED, "isPreloaded.equals=" + UPDATED_IS_PRELOADED);
    }

    @Test
    @Transactional
    void getAllAssignmentsByIsPreloadedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where isPreloaded in
        defaultAssignmentFiltering(
            "isPreloaded.in=" + DEFAULT_IS_PRELOADED + "," + UPDATED_IS_PRELOADED,
            "isPreloaded.in=" + UPDATED_IS_PRELOADED
        );
    }

    @Test
    @Transactional
    void getAllAssignmentsByIsPreloadedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        // Get all the assignmentList where isPreloaded is not null
        defaultAssignmentFiltering("isPreloaded.specified=true", "isPreloaded.specified=false");
    }

    @Test
    @Transactional
    void getAllAssignmentsByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            assignmentRepository.saveAndFlush(assignment);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        assignment.setAppUser(appUser);
        assignmentRepository.saveAndFlush(assignment);
        Long appUserId = appUser.getId();
        // Get all the assignmentList where appUser equals to appUserId
        defaultAssignmentShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the assignmentList where appUser equals to (appUserId + 1)
        defaultAssignmentShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllAssignmentsByStudentClassesIsEqualToSomething() throws Exception {
        StudentClass studentClasses;
        if (TestUtil.findAll(em, StudentClass.class).isEmpty()) {
            assignmentRepository.saveAndFlush(assignment);
            studentClasses = StudentClassResourceIT.createEntity();
        } else {
            studentClasses = TestUtil.findAll(em, StudentClass.class).get(0);
        }
        em.persist(studentClasses);
        em.flush();
        assignment.addStudentClasses(studentClasses);
        assignmentRepository.saveAndFlush(assignment);
        Long studentClassesId = studentClasses.getId();
        // Get all the assignmentList where studentClasses equals to studentClassesId
        defaultAssignmentShouldBeFound("studentClassesId.equals=" + studentClassesId);

        // Get all the assignmentList where studentClasses equals to (studentClassesId + 1)
        defaultAssignmentShouldNotBeFound("studentClassesId.equals=" + (studentClassesId + 1));
    }

    private void defaultAssignmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAssignmentShouldBeFound(shouldBeFound);
        defaultAssignmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssignmentShouldBeFound(String filter) throws Exception {
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].testCases").value(hasItem(DEFAULT_TEST_CASES.toString())))
            .andExpect(jsonPath("$.[*].maxScore").value(hasItem(DEFAULT_MAX_SCORE)))
            .andExpect(jsonPath("$.[*].isPreloaded").value(hasItem(DEFAULT_IS_PRELOADED.booleanValue())));

        // Check, that the count call also returns 1
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssignmentShouldNotBeFound(String filter) throws Exception {
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssignmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssignment() throws Exception {
        // Get the assignment
        restAssignmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssignment() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        assignmentSearchRepository.save(assignment);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());

        // Update the assignment
        Assignment updatedAssignment = assignmentRepository.findById(assignment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAssignment are not directly saved in db
        em.detach(updatedAssignment);
        updatedAssignment
            .title(UPDATED_TITLE)
            .difficulty(UPDATED_DIFFICULTY)
            .description(UPDATED_DESCRIPTION)
            .language(UPDATED_LANGUAGE)
            .testCases(UPDATED_TEST_CASES)
            .maxScore(UPDATED_MAX_SCORE)
            .isPreloaded(UPDATED_IS_PRELOADED);
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(updatedAssignment);

        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assignmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssignmentToMatchAllProperties(updatedAssignment);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Assignment> assignmentSearchList = Streamable.of(assignmentSearchRepository.findAll()).toList();
                Assignment testAssignmentSearch = assignmentSearchList.get(searchDatabaseSizeAfter - 1);

                assertAssignmentAllPropertiesEquals(testAssignmentSearch, updatedAssignment);
            });
    }

    @Test
    @Transactional
    void putNonExistingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assignmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment
            .difficulty(UPDATED_DIFFICULTY)
            .description(UPDATED_DESCRIPTION)
            .language(UPDATED_LANGUAGE)
            .testCases(UPDATED_TEST_CASES)
            .maxScore(UPDATED_MAX_SCORE)
            .isPreloaded(UPDATED_IS_PRELOADED);

        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssignment))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssignmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssignment, assignment),
            getPersistedAssignment(assignment)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssignmentWithPatch() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assignment using partial update
        Assignment partialUpdatedAssignment = new Assignment();
        partialUpdatedAssignment.setId(assignment.getId());

        partialUpdatedAssignment
            .title(UPDATED_TITLE)
            .difficulty(UPDATED_DIFFICULTY)
            .description(UPDATED_DESCRIPTION)
            .language(UPDATED_LANGUAGE)
            .testCases(UPDATED_TEST_CASES)
            .maxScore(UPDATED_MAX_SCORE)
            .isPreloaded(UPDATED_IS_PRELOADED);

        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssignment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssignment))
            )
            .andExpect(status().isOk());

        // Validate the Assignment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssignmentUpdatableFieldsEquals(partialUpdatedAssignment, getPersistedAssignment(partialUpdatedAssignment));
    }

    @Test
    @Transactional
    void patchNonExistingAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assignmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assignmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssignment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assignment.setId(longCount.incrementAndGet());

        // Create the Assignment
        AssignmentDTO assignmentDTO = assignmentMapper.toDto(assignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssignmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assignmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assignment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAssignment() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);
        assignmentRepository.save(assignment);
        assignmentSearchRepository.save(assignment);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the assignment
        restAssignmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, assignment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(assignmentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAssignment() throws Exception {
        // Initialize the database
        insertedAssignment = assignmentRepository.saveAndFlush(assignment);
        assignmentSearchRepository.save(assignment);

        // Search the assignment
        restAssignmentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assignment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assignment.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].testCases").value(hasItem(DEFAULT_TEST_CASES.toString())))
            .andExpect(jsonPath("$.[*].maxScore").value(hasItem(DEFAULT_MAX_SCORE)))
            .andExpect(jsonPath("$.[*].isPreloaded").value(hasItem(DEFAULT_IS_PRELOADED.booleanValue())));
    }

    protected long getRepositoryCount() {
        return assignmentRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Assignment getPersistedAssignment(Assignment assignment) {
        return assignmentRepository.findById(assignment.getId()).orElseThrow();
    }

    protected void assertPersistedAssignmentToMatchAllProperties(Assignment expectedAssignment) {
        assertAssignmentAllPropertiesEquals(expectedAssignment, getPersistedAssignment(expectedAssignment));
    }

    protected void assertPersistedAssignmentToMatchUpdatableProperties(Assignment expectedAssignment) {
        assertAssignmentAllUpdatablePropertiesEquals(expectedAssignment, getPersistedAssignment(expectedAssignment));
    }
}
