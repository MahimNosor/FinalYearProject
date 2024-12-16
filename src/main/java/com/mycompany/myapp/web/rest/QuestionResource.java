package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.QuestionService;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import org.springframework.security.core.Authentication;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.repository.AppUserRepository;
/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Question}.
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final QuestionRepository questionRepository;

    private final UserRepository userRepository;

    private final AppUserRepository appUserRepository;

    public QuestionResource(QuestionService questionService, QuestionRepository questionRepository, UserRepository userRepository, AppUserRepository appUserRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionDTO, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Question : {}", questionDTO);

        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Get the logged-in user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Check if the user is the admin
        if ("admin".equals(currentUserLogin)) {
            LOG.debug("Admin user detected. Proceeding to create a question without linking to AppUser.");
            questionDTO = questionService.save(questionDTO);
        } else {
            // Fetch the current user's details
            User currentUser = userRepository.findOneByLogin(currentUserLogin)
                .orElseThrow(() -> new RuntimeException("User not found for login: " + currentUserLogin));

            // Fetch the AppUser associated with the current user
            AppUser appUser = appUserRepository.findOneByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

            // Check if the user is a teacher
            if (appUser.getRoles().contains("ROLE_TEACHER")) {
                LOG.debug("Teacher user detected. Linking the question to AppUser ID: {}", appUser.getId());
                questionDTO.setAppUserId(appUser.getId()); // Link the question to the teacher's AppUser ID
                questionDTO = questionService.save(questionDTO);
            } else {
                throw new RuntimeException("Unauthorized: Only teachers and the admin can create questions.");
            }
        }

        return ResponseEntity.created(new URI("/api/questions/" + questionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }



    /**
     * {@code PUT  /questions/:id} : Updates an existing question.
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Question : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        questionDTO = questionService.update(questionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString()))
            .body(questionDTO);
    }

    /**
     * {@code PATCH  /questions/:id} : Partial updates given fields of an existing question, field will ignore if it is null
     *
     * @param id the id of the questionDTO to save.
     * @param questionDTO the questionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionDTO,
     * or with status {@code 400 (Bad Request)} if the questionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the questionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuestionDTO> partialUpdateQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuestionDTO questionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Question partially : {}, {}", id, questionDTO);
        if (questionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, questionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!questionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuestionDTO> result = questionService.partialUpdate(questionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("")
    public List<QuestionDTO> getAllQuestions() {
        LOG.debug("REST request to get all Questions");
        return questionService.findAll();
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Question : {}", id);
        Optional<QuestionDTO> questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(questionDTO);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /questions/_search?query=:query} : search for the question corresponding
     * to the query.
     *
     * @param query the query of the question search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<QuestionDTO> searchQuestions(@RequestParam("query") String query) {
        LOG.debug("REST request to search Questions for query {}", query);
        try {
            return questionService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/teacher/questions")
    public ResponseEntity<List<QuestionDTO>> getTeacherQuestions() {
        // Get the currently logged-in user
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Fetch the User entity
        User currentUser = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found for current login"));

        // Fetch the AppUser entity linked to the User
        AppUser teacher = appUserRepository
            .findOneByUserId(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user ID"));

        // Fetch the questions created by the teacher
        List<QuestionDTO> questions = questionService.findAllByTeacher(teacher.getId());

        return ResponseEntity.ok(questions);
    }
}
