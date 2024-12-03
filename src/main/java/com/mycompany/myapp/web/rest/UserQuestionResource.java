package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UserQuestionRepository;
import com.mycompany.myapp.service.UserQuestionService;
import com.mycompany.myapp.service.dto.UserQuestionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
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

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserQuestion}.
 */
@RestController
@RequestMapping("/api/user-questions")
public class UserQuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserQuestionResource.class);

    private static final String ENTITY_NAME = "userQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserQuestionService userQuestionService;

    private final UserQuestionRepository userQuestionRepository;

    public UserQuestionResource(UserQuestionService userQuestionService, UserQuestionRepository userQuestionRepository) {
        this.userQuestionService = userQuestionService;
        this.userQuestionRepository = userQuestionRepository;
    }

    /**
     * {@code POST  /user-questions} : Create a new userQuestion.
     *
     * @param userQuestionDTO the userQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userQuestionDTO, or with status {@code 400 (Bad Request)} if the userQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserQuestionDTO> createUserQuestion(@RequestBody UserQuestionDTO userQuestionDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserQuestion : {}", userQuestionDTO);
        if (userQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userQuestionDTO = userQuestionService.save(userQuestionDTO);
        return ResponseEntity.created(new URI("/api/user-questions/" + userQuestionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userQuestionDTO.getId().toString()))
            .body(userQuestionDTO);
    }

    /**
     * {@code PUT  /user-questions/:id} : Updates an existing userQuestion.
     *
     * @param id the id of the userQuestionDTO to save.
     * @param userQuestionDTO the userQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the userQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserQuestionDTO> updateUserQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserQuestionDTO userQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserQuestion : {}, {}", id, userQuestionDTO);
        if (userQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userQuestionDTO = userQuestionService.update(userQuestionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userQuestionDTO.getId().toString()))
            .body(userQuestionDTO);
    }

    /**
     * {@code PATCH  /user-questions/:id} : Partial updates given fields of an existing userQuestion, field will ignore if it is null
     *
     * @param id the id of the userQuestionDTO to save.
     * @param userQuestionDTO the userQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the userQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserQuestionDTO> partialUpdateUserQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserQuestionDTO userQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserQuestion partially : {}, {}", id, userQuestionDTO);
        if (userQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserQuestionDTO> result = userQuestionService.partialUpdate(userQuestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userQuestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-questions} : get all the userQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userQuestions in body.
     */
    @GetMapping("")
    public List<UserQuestionDTO> getAllUserQuestions() {
        LOG.debug("REST request to get all UserQuestions");
        return userQuestionService.findAll();
    }

    /**
     * {@code GET  /user-questions/:id} : get the "id" userQuestion.
     *
     * @param id the id of the userQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserQuestionDTO> getUserQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserQuestion : {}", id);
        Optional<UserQuestionDTO> userQuestionDTO = userQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userQuestionDTO);
    }

    /**
     * {@code DELETE  /user-questions/:id} : delete the "id" userQuestion.
     *
     * @param id the id of the userQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserQuestion : {}", id);
        userQuestionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-questions/_search?query=:query} : search for the userQuestion corresponding
     * to the query.
     *
     * @param query the query of the userQuestion search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<UserQuestionDTO> searchUserQuestions(@RequestParam("query") String query) {
        LOG.debug("REST request to search UserQuestions for query {}", query);
        try {
            return userQuestionService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
