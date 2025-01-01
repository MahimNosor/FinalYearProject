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
    public AssignmentDTO save(AssignmentDTO assignmentDTO) {
        LOG.debug("Request to save Assignment : {}", assignmentDTO);
        Assignment assignment = assignmentMapper.toEntity(assignmentDTO);
        assignment = assignmentRepository.save(assignment);
        assignmentSearchRepository.index(assignment);
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
}
