package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.AppUserService;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.TeacherDashboardDTO;
import com.mycompany.myapp.service.mapper.AppUserMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import com.mycompany.myapp.security.SecurityUtils;
import java.util.Map;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AppUser}.
 */
@RestController
@RequestMapping("/api/app-users")
public class AppUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppUserResource.class);

    private static final String ENTITY_NAME = "appUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppUserService appUserService;

    private final AppUserRepository appUserRepository;

    private final UserRepository userRepository;

    private final AppUserMapper appUserMapper;

    public AppUserResource(
        AppUserService appUserService,
        AppUserRepository appUserRepository,
        UserRepository userRepository,
        AppUserMapper appUserMapper // Include appUserMapper in the constructor
    ) {
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
        this.userRepository = userRepository;
        this.appUserMapper = appUserMapper; // Initialize appUserMapper properly
    }

    /**
     * {@code POST  /app-users} : Create a new appUser.
     *
     * @param appUserDTO the appUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appUserDTO, or with status {@code 400 (Bad Request)} if the appUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppUserDTO> createAppUser(@Valid @RequestBody AppUserDTO appUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save AppUser : {}", appUserDTO);
        if (appUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new appUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (appUserDTO.getUserId() != null && !userRepository.existsById(appUserDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid userId", ENTITY_NAME, "usernotfound");
        }
        appUserDTO = appUserService.save(appUserDTO);
        return ResponseEntity.created(new URI("/api/app-users/" + appUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appUserDTO.getId().toString()))
            .body(appUserDTO);
    }

    /**
     * {@code PUT  /app-users/:id} : Updates an existing appUser.
     *
     * @param id the id of the appUserDTO to save.
     * @param appUserDTO the appUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appUserDTO,
     * or with status {@code 400 (Bad Request)} if the appUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateAppUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppUserDTO appUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppUser : {}, {}", id, appUserDTO);
        if (appUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!appUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (appUserDTO.getUserId() != null && !userRepository.existsById(appUserDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid userId", ENTITY_NAME, "usernotfound");
        }
        appUserDTO = appUserService.update(appUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUserDTO.getId().toString()))
            .body(appUserDTO);
    }

    /**
     * {@code PATCH  /app-users/:id} : Partial updates given fields of an existing appUser, field will ignore if it is null
     *
     * @param id the id of the appUserDTO to save.
     * @param appUserDTO the appUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appUserDTO,
     * or with status {@code 400 (Bad Request)} if the appUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppUserDTO> partialUpdateAppUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppUserDTO appUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppUser partially : {}, {}", id, appUserDTO);
        if (appUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!appUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (appUserDTO.getUserId() != null && !userRepository.existsById(appUserDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid userId", ENTITY_NAME, "usernotfound");
        }
        Optional<AppUserDTO> result = appUserService.partialUpdate(appUserDTO);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-users} : get all the appUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appUsers in body.
     */
    @GetMapping("")
    public List<AppUserDTO> getAllAppUsers(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all AppUsers");
        return appUserService.findAll();
    }

    /**
     * {@code GET  /app-users/:id} : get the "id" appUser.
     *
     * @param id the id of the appUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getAppUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AppUser : {}", id);
        Optional<AppUserDTO> appUserDTO = appUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appUserDTO);
    }

    /**
     * {@code DELETE  /app-users/:id} : delete the "id" appUser.
     *
     * @param id the id of the appUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AppUser : {}", id);
        appUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /app-users/_search?query=:query} : search for the appUser corresponding
     * to the query.
     *
     * @param query the query of the appUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<AppUserDTO> searchAppUsers(@RequestParam("query") String query) {
        LOG.debug("REST request to search AppUsers for query {}", query);
        try {
            return appUserService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }

    @GetMapping("/dashboard")
    public TeacherDashboardDTO getDashboard(Authentication authentication) {
        return appUserService.getDashboardStats(authentication);
    }

    @GetMapping("/leaderboard")
    public List<AppUserDTO> getLeaderboard() {
        LOG.debug("REST request to get leaderboard");
        return appUserService.findAllSortedByPoints();
    }

    @GetMapping("/account")
    public ResponseEntity<AppUserDTO> getAccount(Authentication authentication) {
        Optional<AppUser> user = appUserService.getCurrentUser(authentication);
        if (user.isPresent()) {
            AppUserDTO appUserDTO = appUserMapper.toDto(user.get());
            return ResponseEntity.ok(appUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add-points")
    public ResponseEntity<Void> addPoints(@RequestParam("points") int points, Authentication authentication) {
        LOG.debug("REST request to add points: {}", points);
        appUserService.addPointsToUser(authentication, points);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/leaderboard/class/{classId}")
    public List<AppUserDTO> getClassLeaderboard(@PathVariable Long classId) {
        LOG.debug("REST request to get leaderboard for class ID: {}", classId);
        return appUserService.getClassLeaderboard(classId);
    }

    @GetMapping("/current-app-user")
    public ResponseEntity<AppUserDTO> getCurrentAppUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        AppUser appUser = appUserRepository.findOneByUserIdWithClasses(
            userRepository.findOneByLogin(currentUserLogin)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId()
        ).orElseThrow(() -> new RuntimeException("AppUser not found"));

        return ResponseEntity.ok(appUserMapper.toDto(appUser));
    }
    @GetMapping("/teacher-dashboard/metrics")
    public ResponseEntity<Map<String, Integer>> getTeacherDashboardMetrics() {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        AppUser teacher = appUserRepository.findByUser_Login(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

        Map<String, Integer> metrics = appUserService.getTeacherDashboardMetrics(teacher.getId());
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/teacher-dashboard/students")
    public ResponseEntity<List<AppUserDTO>> getDistinctStudentsForTeacher() {
        LOG.debug("REST request to get distinct students for the logged-in teacher");

        // Fetch the current logged-in teacher
        String currentUserLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        AppUser teacher = appUserRepository.findByUser_Login(currentUserLogin)
            .orElseThrow(() -> new RuntimeException("Teacher not found for the current user"));

        // Call the service method to get distinct students
        List<AppUserDTO> students = appUserService.getDistinctStudentsForTeacher(teacher.getId());

        return ResponseEntity.ok(students);
    }

}
