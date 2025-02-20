package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AssignmentRepository;
import com.mycompany.myapp.service.AssignmentQueryService;
import com.mycompany.myapp.service.AssignmentService;
import com.mycompany.myapp.service.criteria.AssignmentCriteria;
import com.mycompany.myapp.service.dto.AssignmentDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.StudentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.mapper.AppUserMapper;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Assignment}.
 */
@RestController
@RequestMapping("/api/assignments")
public class AssignmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentResource.class);

    private static final String ENTITY_NAME = "assignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssignmentService assignmentService;

    private final AssignmentRepository assignmentRepository;

    private final AssignmentQueryService assignmentQueryService;

    private final AppUserMapper appUserMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppUserRepository appUserRepository;


    public AssignmentResource(
        AssignmentService assignmentService,
        AssignmentRepository assignmentRepository,
        AssignmentQueryService assignmentQueryService,
        AppUserMapper appUserMapper,
        AppUserRepository appUserRepository
    ) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.assignmentQueryService = assignmentQueryService;
        this.appUserMapper = appUserMapper;
        this.appUserRepository = appUserRepository;
    }

    /**
     * {@code POST  /assignments} : Create a new assignment.
     *
     * @param assignmentDTO the assignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assignmentDTO, or with status {@code 400 (Bad Request)} if the assignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody AssignmentDTO assignmentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Assignment : {}", assignmentDTO);

        // Fetch current user's AppUser ID
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));


        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found for login: " + currentUserLogin));

        AppUser appUser = appUserRepository.findOneByUserIdWithClasses(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

        // Assign the assignment to the teacher
        if (appUser.getRoles().contains("ROLE_TEACHER")) {
            assignmentDTO.setAppUser(appUserMapper.toDto(appUser));
        }
        AssignmentDTO result = assignmentService.save(assignmentDTO);
        return ResponseEntity
            .created(new URI("/api/assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, "Assignment", result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assignments/:id} : Updates an existing assignment.
     *
     * @param id the id of the assignmentDTO to save.
     * @param assignmentDTO the assignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssignmentDTO assignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Assignment : {}, {}", id, assignmentDTO);
        if (assignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assignmentDTO = assignmentService.update(assignmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignmentDTO.getId().toString()))
            .body(assignmentDTO);
    }

    /**
     * {@code PATCH  /assignments/:id} : Partial updates given fields of an existing assignment, field will ignore if it is null
     *
     * @param id the id of the assignmentDTO to save.
     * @param assignmentDTO the assignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assignmentDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssignmentDTO> partialUpdateAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssignmentDTO assignmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Assignment partially : {}, {}", id, assignmentDTO);
        if (assignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssignmentDTO> result = assignmentService.partialUpdate(assignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assignments} : get all the assignments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assignments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments(
        AssignmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Assignments by criteria: {}", criteria);

        Page<AssignmentDTO> page = assignmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assignments/count} : count all the assignments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAssignments(AssignmentCriteria criteria) {
        LOG.debug("REST request to count Assignments by criteria: {}", criteria);
        return ResponseEntity.ok().body(assignmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /assignments/:id} : get the "id" assignment.
     *
     * @param id the id of the assignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Assignment : {}", id);
        Optional<AssignmentDTO> assignmentDTO = assignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignmentDTO);
    }

    /**
     * {@code DELETE  /assignments/:id} : delete the "id" assignment.
     *
     * @param id the id of the assignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Assignment : {}", id);
        assignmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /assignments/_search?query=:query} : search for the assignment corresponding
     * to the query.
     *
     * @param query the query of the assignment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<AssignmentDTO>> searchAssignments(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Assignments for query {}", query);
        try {
            Page<AssignmentDTO> page = assignmentService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/teacher/assignments")
    public ResponseEntity<List<AssignmentDTO>> getTeacherAssignments() {
        // Fetch current user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Fetch the current user
        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found for current login"));

        // Fetch the AppUser for the logged-in user
        AppUser teacher = appUserRepository.findOneByUserId(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user ID"));

        // Fetch assignments created by the teacher
        List<AssignmentDTO> assignments = assignmentService.findAllByAppUserId(teacher.getId());

        return ResponseEntity.ok(assignments);
    }

}
