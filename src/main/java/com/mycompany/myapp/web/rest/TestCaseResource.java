package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.TestCaseRepository;
import com.mycompany.myapp.service.TestCaseService;
import com.mycompany.myapp.service.dto.TestCaseDTO;
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

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TestCase}.
 */
@RestController
@RequestMapping("/api/test-cases")
public class TestCaseResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseResource.class);

    private static final String ENTITY_NAME = "testCase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCaseService testCaseService;

    private final TestCaseRepository testCaseRepository;

    public TestCaseResource(TestCaseService testCaseService, TestCaseRepository testCaseRepository) {
        this.testCaseService = testCaseService;
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * {@code POST  /test-cases} : Create a new testCase.
     *
     * @param testCaseDTO the testCaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCaseDTO, or with status {@code 400 (Bad Request)} if the testCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCaseDTO> createTestCase(@Valid @RequestBody TestCaseDTO testCaseDTO) throws URISyntaxException {
        LOG.debug("REST request to save TestCase : {}", testCaseDTO);
        if (testCaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new testCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testCaseDTO = testCaseService.save(testCaseDTO);
        return ResponseEntity.created(new URI("/api/test-cases/" + testCaseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testCaseDTO.getId().toString()))
            .body(testCaseDTO);
    }

    /**
     * {@code PUT  /test-cases/:id} : Updates an existing testCase.
     *
     * @param id the id of the testCaseDTO to save.
     * @param testCaseDTO the testCaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCaseDTO> updateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TestCaseDTO testCaseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TestCase : {}, {}", id, testCaseDTO);
        if (testCaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testCaseDTO = testCaseService.update(testCaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCaseDTO.getId().toString()))
            .body(testCaseDTO);
    }

    /**
     * {@code PATCH  /test-cases/:id} : Partial updates given fields of an existing testCase, field will ignore if it is null
     *
     * @param id the id of the testCaseDTO to save.
     * @param testCaseDTO the testCaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCaseDTO,
     * or with status {@code 400 (Bad Request)} if the testCaseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the testCaseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCaseDTO> partialUpdateTestCase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TestCaseDTO testCaseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TestCase partially : {}, {}", id, testCaseDTO);
        if (testCaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCaseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCaseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCaseDTO> result = testCaseService.partialUpdate(testCaseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCaseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /test-cases} : get all the testCases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCases in body.
     */
    @GetMapping("")
    public List<TestCaseDTO> getAllTestCases() {
        LOG.debug("REST request to get all TestCases");
        return testCaseService.findAll();
    }

    /**
     * {@code GET  /test-cases/:id} : get the "id" testCase.
     *
     * @param id the id of the testCaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCaseDTO> getTestCase(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TestCase : {}", id);
        Optional<TestCaseDTO> testCaseDTO = testCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCaseDTO);
    }

    /**
     * {@code DELETE  /test-cases/:id} : delete the "id" testCase.
     *
     * @param id the id of the testCaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCase(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TestCase : {}", id);
        testCaseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /test-cases/_search?query=:query} : search for the testCase corresponding
     * to the query.
     *
     * @param query the query of the testCase search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<TestCaseDTO> searchTestCases(@RequestParam("query") String query) {
        LOG.debug("REST request to search TestCases for query {}", query);
        try {
            return testCaseService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
