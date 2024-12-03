package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.search.QuestionSearchRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.mapper.QuestionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Question}.
 */
@Service
@Transactional
public class QuestionService {

    @Autowired
    private AppUserRepository appUserRepository;

    private UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final QuestionSearchRepository questionSearchRepository;

    public QuestionService(
        QuestionRepository questionRepository,
        QuestionMapper questionMapper,
        QuestionSearchRepository questionSearchRepository
    ) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
        this.questionSearchRepository = questionSearchRepository;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionDTO save(QuestionDTO questionDTO) {
        LOG.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        questionSearchRepository.index(question);
        return questionMapper.toDto(question);
    }

    /**
     * Update a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    public QuestionDTO update(QuestionDTO questionDTO) {
        LOG.debug("Request to update Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        questionSearchRepository.index(question);
        return questionMapper.toDto(question);
    }

    /**
     * Partially update a question.
     *
     * @param questionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<QuestionDTO> partialUpdate(QuestionDTO questionDTO) {
        LOG.debug("Request to partially update Question : {}", questionDTO);

        return questionRepository
            .findById(questionDTO.getId())
            .map(existingQuestion -> {
                questionMapper.partialUpdate(existingQuestion, questionDTO);

                return existingQuestion;
            })
            .map(questionRepository::save)
            .map(savedQuestion -> {
                questionSearchRepository.index(savedQuestion);
                return savedQuestion;
            })
            .map(questionMapper::toDto);
    }

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> findAll() {
        LOG.debug("Request to get all Questions");
        return questionRepository.findAll().stream().map(questionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionDTO> findOne(Long id) {
        LOG.debug("Request to get Question : {}", id);
        return questionRepository.findById(id).map(questionMapper::toDto);
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
        questionSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the question corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionDTO> search(String query) {
        LOG.debug("Request to search Questions for query {}", query);
        try {
            return StreamSupport.stream(questionSearchRepository.search(query).spliterator(), false).map(questionMapper::toDto).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public List<QuestionDTO> findAllForLoggedInTeacher() {
        LOG.debug("Request to get all Questions for the logged-in teacher");

        // Step 1: Get the current logged-in user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("User not logged in"));

        // Step 2: Find the User entity by login
        User currentUser = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Step 3: Find the associated AppUser entity using the login
        AppUser teacher = appUserRepository
            .findByUser_Login(currentUserLogin)
            .orElseThrow(() -> new IllegalArgumentException("AppUser not found"));

        // Step 4: Use QuestionRepository to find questions by teacher ID
        List<Question> questions = questionRepository.findByTeacherId(teacher.getId());

        // Step 5: Map the list of Question entities to a list of QuestionDTO
        return questions.stream().map(questionMapper::toDto).collect(Collectors.toList());
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        LOG.debug("Request to create Question : {}", questionDTO);

        // Step 1: Get the current logged-in user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalArgumentException("User not logged in"));

        // Step 2: Find the User entity by login
        User currentUser = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Step 3: Find the associated AppUser entity using the login
        AppUser teacher = appUserRepository
            .findByUser_Login(currentUserLogin)
            .orElseThrow(() -> new IllegalArgumentException("AppUser not found"));

        // Step 4: Map the QuestionDTO to Question entity and set the teacher
        Question question = questionMapper.toEntity(questionDTO);
        question.setTeacher(teacher);
        question = questionRepository.save(question);

        // Step 5: Return the saved question as a DTO
        return questionMapper.toDto(question);
    }
}
