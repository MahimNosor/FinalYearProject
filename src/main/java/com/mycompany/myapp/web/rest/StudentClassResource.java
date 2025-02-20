package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.StudentClassRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.StudentClassService;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.AssignmentDTO;


/**
 * REST controller for managing {@link com.mycompany.myapp.domain.StudentClass}.
 */
@RestController
@RequestMapping("/api/student-classes")
public class StudentClassResource {

    private static final Logger LOG = LoggerFactory.getLogger(StudentClassResource.class);

    private static final String ENTITY_NAME = "studentClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentClassService studentClassService;

    private final StudentClassRepository studentClassRepository;
    private final Logger log = LoggerFactory.getLogger(StudentClassResource.class);

    private final AppUserRepository appUserRepository;

    private final UserRepository userRepository;

    public StudentClassResource(
        StudentClassService studentClassService,
        StudentClassRepository studentClassRepository,
        AppUserRepository appUserRepository,
        UserRepository userRepository
    ) {
        this.studentClassService = studentClassService;
        this.studentClassRepository = studentClassRepository;
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /student-classes} : Create a new studentClass.
     *
     * @param studentClassDTO the studentClassDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentClassDTO, or with status {@code 400 (Bad Request)} if the studentClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    
    @PostMapping("")
    public ResponseEntity<StudentClassDTO> createStudentClass(@Valid @RequestBody StudentClassDTO studentClassDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save StudentClass : {}", studentClassDTO);
        if (studentClassDTO.getId() != null) {
            throw new BadRequestAlertException("A new studentClass cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // Get the logged-in user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Fetch the current user's AppUser
        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found for login: " + currentUserLogin));

        AppUser appUser = appUserRepository.findOneByUserId(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

        // If the user is a teacher, link the class to their AppUserID
        if (appUser.getRoles().contains("ROLE_TEACHER")) {
            studentClassDTO.setAppUserId(appUser.getId());
        }

        studentClassDTO = studentClassService.save(studentClassDTO);
        return ResponseEntity.created(new URI("/api/student-classes/" + studentClassDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString()))
            .body(studentClassDTO);
    }

    /**
     * {@code PUT  /student-classes/:id} : Updates an existing studentClass.
     *
     * @param id the id of the studentClassDTO to save.
     * @param studentClassDTO the studentClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClassDTO,
     * or with status {@code 400 (Bad Request)} if the studentClassDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudentClassDTO> updateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudentClassDTO studentClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update StudentClass : {}, {}", id, studentClassDTO);
        if (studentClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        studentClassDTO = studentClassService.update(studentClassDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString()))
            .body(studentClassDTO);
    }

    /**
     * {@code PATCH  /student-classes/:id} : Partial updates given fields of an existing studentClass, field will ignore if it is null
     *
     * @param id the id of the studentClassDTO to save.
     * @param studentClassDTO the studentClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentClassDTO,
     * or with status {@code 400 (Bad Request)} if the studentClassDTO is not valid,
     * or with status {@code 404 (Not Found)} if the studentClassDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the studentClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudentClassDTO> partialUpdateStudentClass(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudentClassDTO studentClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update StudentClass partially : {}, {}", id, studentClassDTO);
        if (studentClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studentClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studentClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudentClassDTO> result = studentClassService.partialUpdate(studentClassDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentClassDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /student-classes} : get all the studentClasses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentClasses in body.
     */
    @GetMapping("")
    public List<StudentClassDTO> getAllStudentClasses() {
        LOG.debug("REST request to get all StudentClasses");
        return studentClassService.findAll();
    }

    /**
     * {@code GET  /student-classes/:id} : get the "id" studentClass.
     *
     * @param id the id of the studentClassDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentClassDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentClassDTO> getStudentClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to get StudentClass : {}", id);
        Optional<StudentClassDTO> studentClassDTO = studentClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentClassDTO);
    }

    /**
     * {@code DELETE  /student-classes/:id} : delete the "id" studentClass.
     *
     * @param id the id of the studentClassDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete StudentClass : {}", id);
        studentClassService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /student-classes/_search?query=:query} : search for the studentClass corresponding
     * to the query.
     *
     * @param query the query of the studentClass search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<StudentClassDTO> searchStudentClasses(@RequestParam("query") String query) {
        LOG.debug("REST request to search StudentClasses for query {}", query);
        try {
            return studentClassService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/student/current")
    public List<StudentClassDTO> getStudentClassesForLoggedInUser() {
        // Fetch the current user's login
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        // Fetch the current user
        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("User not found for login: " + currentUserLogin));

        // Fetch the associated AppUser
        AppUser appUser = appUserRepository.findOneByUserId(currentUser.getId())
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

        // Call the service layer with the AppUser ID
        return studentClassService.findClassesForStudent(appUser.getId());
    }


    @GetMapping("/teacher/classes")
    public ResponseEntity<List<StudentClassDTO>> getTeacherClasses() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        User currentUser = userRepository
        .findOneByLogin(currentUserLogin)
        .orElseThrow(() -> new RuntimeException("User not found for current login"));

        AppUser teacher = appUserRepository
        .findOneByUserId(currentUser.getId())
        .orElseThrow(() -> new RuntimeException("AppUser not found for current user ID"));

        List<StudentClassDTO> classes = studentClassService.getClassesByTeacher(teacher.getId());

        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}/assignments")
    public List<AssignmentDTO> getAssignmentsForClass(@PathVariable Long id) {
        return studentClassService.findAssignmentsByClassId(id);
    }

}
