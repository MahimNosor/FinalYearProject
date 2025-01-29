package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserQuestion;
import com.mycompany.myapp.repository.UserQuestionRepository;
import com.mycompany.myapp.repository.search.UserQuestionSearchRepository;
import com.mycompany.myapp.service.dto.UserQuestionDTO;
import com.mycompany.myapp.service.mapper.UserQuestionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserQuestion}.
 */
@Service
@Transactional
public class UserQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(UserQuestionService.class);

    private final UserQuestionRepository userQuestionRepository;

    private final UserQuestionMapper userQuestionMapper;

    private final UserQuestionSearchRepository userQuestionSearchRepository;

    private final Logger log = LoggerFactory.getLogger(UserQuestionService.class);


    public UserQuestionService(
        UserQuestionRepository userQuestionRepository,
        UserQuestionMapper userQuestionMapper,
        UserQuestionSearchRepository userQuestionSearchRepository
    ) {
        this.userQuestionRepository = userQuestionRepository;
        this.userQuestionMapper = userQuestionMapper;
        this.userQuestionSearchRepository = userQuestionSearchRepository;
    }

    /**
     * Save a userQuestion.
     *
     * @param userQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserQuestionDTO save(UserQuestionDTO userQuestionDTO) {
        LOG.debug("Request to save UserQuestion : {}", userQuestionDTO);
        UserQuestion userQuestion = userQuestionMapper.toEntity(userQuestionDTO);
        userQuestion = userQuestionRepository.save(userQuestion);
        userQuestionSearchRepository.index(userQuestion);
        return userQuestionMapper.toDto(userQuestion);
    }

    /**
     * Update a userQuestion.
     *
     * @param userQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    public UserQuestionDTO update(UserQuestionDTO userQuestionDTO) {
        LOG.debug("Request to update UserQuestion : {}", userQuestionDTO);
        UserQuestion userQuestion = userQuestionMapper.toEntity(userQuestionDTO);
        userQuestion = userQuestionRepository.save(userQuestion);
        userQuestionSearchRepository.index(userQuestion);
        return userQuestionMapper.toDto(userQuestion);
    }

    /**
     * Partially update a userQuestion.
     *
     * @param userQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserQuestionDTO> partialUpdate(UserQuestionDTO userQuestionDTO) {
        LOG.debug("Request to partially update UserQuestion : {}", userQuestionDTO);

        return userQuestionRepository
            .findById(userQuestionDTO.getId())
            .map(existingUserQuestion -> {
                userQuestionMapper.partialUpdate(existingUserQuestion, userQuestionDTO);

                return existingUserQuestion;
            })
            .map(userQuestionRepository::save)
            .map(savedUserQuestion -> {
                userQuestionSearchRepository.index(savedUserQuestion);
                return savedUserQuestion;
            })
            .map(userQuestionMapper::toDto);
    }

    /**
     * Get all the userQuestions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserQuestionDTO> findAll() {
        LOG.debug("Request to get all UserQuestions");
        return userQuestionRepository.findAll().stream().map(userQuestionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one userQuestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserQuestionDTO> findOne(Long id) {
        LOG.debug("Request to get UserQuestion : {}", id);
        return userQuestionRepository.findById(id).map(userQuestionMapper::toDto);
    }

    /**
     * Delete the userQuestion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserQuestion : {}", id);
        userQuestionRepository.deleteById(id);
        userQuestionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the userQuestion corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserQuestionDTO> search(String query) {
        LOG.debug("Request to search UserQuestions for query {}", query);
        try {
            return StreamSupport.stream(userQuestionSearchRepository.search(query).spliterator(), false)
                .map(userQuestionMapper::toDto)
                .toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<UserQuestionDTO> findByAppUserId(Long appUserId) {
        log.debug("Fetching UserQuestion submissions for AppUser ID: {}", appUserId);
        
        List<UserQuestion> userQuestions = userQuestionRepository.findByAppUserIdOrderBySubmissionDateDesc(appUserId);
        
        return userQuestions.stream()
            .map(userQuestionMapper::toDto)
            .collect(Collectors.toList());
    }

}
