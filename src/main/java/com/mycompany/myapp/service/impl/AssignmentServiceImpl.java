package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.repository.AssignmentRepository;
import com.mycompany.myapp.repository.search.AssignmentSearchRepository;
import com.mycompany.myapp.service.AssignmentService;
import com.mycompany.myapp.service.dto.AssignmentDTO;
import com.mycompany.myapp.service.mapper.AssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.AppUser;
import org.springframework.security.access.AccessDeniedException;
import java.util.HashSet;
import java.util.List;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.AppUserRepository;
import com.mycompany.myapp.repository.StudentClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.domain.StudentClass;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Assignment}.
 */
@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentServiceImpl.class);

    private final AssignmentRepository assignmentRepository;

    private final AssignmentMapper assignmentMapper;

    private final AssignmentSearchRepository assignmentSearchRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private StudentClassRepository studentClassRepository;

    public AssignmentServiceImpl(
        AssignmentRepository assignmentRepository,
        AssignmentMapper assignmentMapper,
        AssignmentSearchRepository assignmentSearchRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.assignmentSearchRepository = assignmentSearchRepository;
    }

    @Override
@Transactional
public AssignmentDTO save(AssignmentDTO assignmentDTO) {
    LOG.debug("Request to save Assignment : {}", assignmentDTO);

    // Map DTO to entity
    Assignment assignment = assignmentMapper.toEntity(assignmentDTO);

    // Fetch current user's login
    String currentUserLogin = SecurityUtils.getCurrentUserLogin()
        .orElseThrow(() -> new RuntimeException("Current user login not found"));

    // Get the current user
    User currentUser = userRepository.findOneByLogin(currentUserLogin)
        .orElseThrow(() -> new RuntimeException("User not found for login: " + currentUserLogin));

    // Get the corresponding AppUser
    AppUser appUser = appUserRepository.findOneByUserId(currentUser.getId())
        .orElseThrow(() -> new RuntimeException("AppUser not found for current user"));

    // Handle preloaded assignments
    if (assignmentDTO.getIsPreloaded()) {
        if (!SecurityUtils.hasCurrentUserThisAuthority("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can create preloaded assignments");
        }
        // Assign preloaded assignments to all classes
        List<StudentClass> allClasses = studentClassRepository.findAll();
        assignment.setStudentClasses(new HashSet<>(allClasses));
    } else {
        // For non-preloaded assignments, link them to the teacher
        assignment.setAppUser(appUser);
    }

    // Save the assignment
    assignment = assignmentRepository.save(assignment);
    assignmentSearchRepository.index(assignment);

    // Map the saved entity back to DTO
    return assignmentMapper.toDto(assignment);
}


    @Override
    public AssignmentDTO update(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to update Assignment : {}", assignmentDTO);
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);
        assignment = assignmentRepository.save(assignment);
        assignmentSearchRepository.index(assignment);
        return assignmentMapper.toDto(assignment);
    }

    @Override
    public Optional<AssignmentDTO> partialUpdate(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to partially update Assignment : {}", assignmentDTO);

        return assignmentRepository
            .findById(assignmentDTO.getId())
            .map(existingAssignment -> {
                assignmentMapper.partialUpdate(existingAssignment, assignmentDTO);

                return existingAssignment;
            })
            .map(assignmentRepository::save)
            .map(savedAssignment -> {
                assignmentSearchRepository.index(savedAssignment);
                return savedAssignment;
            })
            .map(assignmentMapper::toDto);
    }

    public Page<AssignmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assignmentRepository.findAllWithEagerRelationships(pageable).map(assignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssignmentDTO> findOne(Long id) {
        LOG.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findOneWithEagerRelationships(id).map(assignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Assignment : {}", id);
        assignmentRepository.deleteById(id);
        assignmentSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssignmentDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Assignments for query {}", query);
        return assignmentSearchRepository.search(query, pageable).map(assignmentMapper::toDto);
    }

    @Override
    public List<AssignmentDTO> findAllByAppUserId(Long appUserId) {
        LOG.debug("Request to get Assignments for AppUser ID: {}", appUserId);
        return assignmentRepository.findByAppUserId(appUserId).stream()
            .map(assignmentMapper::toDto)
            .collect(Collectors.toList());
    }


}
