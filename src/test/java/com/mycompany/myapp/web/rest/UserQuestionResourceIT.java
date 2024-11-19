package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserQuestionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserQuestion;
import com.mycompany.myapp.domain.enumeration.SubmissionStatus;
import com.mycompany.myapp.repository.UserQuestionRepository;
import com.mycompany.myapp.repository.search.UserQuestionSearchRepository;
import com.mycompany.myapp.service.dto.UserQuestionDTO;
import com.mycompany.myapp.service.mapper.UserQuestionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserQuestionResourceIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final Instant DEFAULT_SUBMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SubmissionStatus DEFAULT_STATUS = SubmissionStatus.PENDING;
    private static final SubmissionStatus UPDATED_STATUS = SubmissionStatus.SUBMITTED;

    private static final String ENTITY_API_URL = "/api/user-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-questions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private UserQuestionMapper userQuestionMapper;

    @Autowired
    private UserQuestionSearchRepository userQuestionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserQuestionMockMvc;

    private UserQuestion userQuestion;

    private UserQuestion insertedUserQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQuestion createEntity() {
        return new UserQuestion().score(DEFAULT_SCORE).submissionDate(DEFAULT_SUBMISSION_DATE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserQuestion createUpdatedEntity() {
        return new UserQuestion().score(UPDATED_SCORE).submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        userQuestion = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserQuestion != null) {
            userQuestionRepository.delete(insertedUserQuestion);
            userQuestionSearchRepository.delete(insertedUserQuestion);
            insertedUserQuestion = null;
        }
    }

    @Test
    @Transactional
    void createUserQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);
        var returnedUserQuestionDTO = om.readValue(
            restUserQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userQuestionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserQuestionDTO.class
        );

        // Validate the UserQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserQuestion = userQuestionMapper.toEntity(returnedUserQuestionDTO);
        assertUserQuestionUpdatableFieldsEquals(returnedUserQuestion, getPersistedUserQuestion(returnedUserQuestion));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUserQuestion = returnedUserQuestion;
    }

    @Test
    @Transactional
    void createUserQuestionWithExistingId() throws Exception {
        // Create the UserQuestion with an existing ID
        userQuestion.setId(1L);
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userQuestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUserQuestions() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);

        // Get all the userQuestionList
        restUserQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUserQuestion() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);

        // Get the userQuestion
        restUserQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, userQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userQuestion.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserQuestion() throws Exception {
        // Get the userQuestion
        restUserQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserQuestion() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        userQuestionSearchRepository.save(userQuestion);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());

        // Update the userQuestion
        UserQuestion updatedUserQuestion = userQuestionRepository.findById(userQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserQuestion are not directly saved in db
        em.detach(updatedUserQuestion);
        updatedUserQuestion.score(UPDATED_SCORE).submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(updatedUserQuestion);

        restUserQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserQuestionToMatchAllProperties(updatedUserQuestion);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserQuestion> userQuestionSearchList = Streamable.of(userQuestionSearchRepository.findAll()).toList();
                UserQuestion testUserQuestionSearch = userQuestionSearchList.get(searchDatabaseSizeAfter - 1);

                assertUserQuestionAllPropertiesEquals(testUserQuestionSearch, updatedUserQuestion);
            });
    }

    @Test
    @Transactional
    void putNonExistingUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUserQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userQuestion using partial update
        UserQuestion partialUpdatedUserQuestion = new UserQuestion();
        partialUpdatedUserQuestion.setId(userQuestion.getId());

        partialUpdatedUserQuestion.submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);

        restUserQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserQuestion))
            )
            .andExpect(status().isOk());

        // Validate the UserQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserQuestion, userQuestion),
            getPersistedUserQuestion(userQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userQuestion using partial update
        UserQuestion partialUpdatedUserQuestion = new UserQuestion();
        partialUpdatedUserQuestion.setId(userQuestion.getId());

        partialUpdatedUserQuestion.score(UPDATED_SCORE).submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);

        restUserQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserQuestion))
            )
            .andExpect(status().isOk());

        // Validate the UserQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserQuestionUpdatableFieldsEquals(partialUpdatedUserQuestion, getPersistedUserQuestion(partialUpdatedUserQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        userQuestion.setId(longCount.incrementAndGet());

        // Create the UserQuestion
        UserQuestionDTO userQuestionDTO = userQuestionMapper.toDto(userQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUserQuestion() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);
        userQuestionRepository.save(userQuestion);
        userQuestionSearchRepository.save(userQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userQuestion
        restUserQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userQuestionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUserQuestion() throws Exception {
        // Initialize the database
        insertedUserQuestion = userQuestionRepository.saveAndFlush(userQuestion);
        userQuestionSearchRepository.save(userQuestion);

        // Search the userQuestion
        restUserQuestionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + userQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return userQuestionRepository.count();
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

    protected UserQuestion getPersistedUserQuestion(UserQuestion userQuestion) {
        return userQuestionRepository.findById(userQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedUserQuestionToMatchAllProperties(UserQuestion expectedUserQuestion) {
        assertUserQuestionAllPropertiesEquals(expectedUserQuestion, getPersistedUserQuestion(expectedUserQuestion));
    }

    protected void assertPersistedUserQuestionToMatchUpdatableProperties(UserQuestion expectedUserQuestion) {
        assertUserQuestionAllUpdatablePropertiesEquals(expectedUserQuestion, getPersistedUserQuestion(expectedUserQuestion));
    }
}
