package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AssignmentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Assignment}.
 */
public interface AssignmentService {
    /**
     * Save a assignment.
     *
     * @param assignmentDTO the entity to save.
     * @return the persisted entity.
     */
    AssignmentDTO save(AssignmentDTO assignmentDTO);

    /**
     * Updates a assignment.
     *
     * @param assignmentDTO the entity to update.
     * @return the persisted entity.
     */
    AssignmentDTO update(AssignmentDTO assignmentDTO);

    /**
     * Partially updates a assignment.
     *
     * @param assignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AssignmentDTO> partialUpdate(AssignmentDTO assignmentDTO);

    /**
     * Get all the assignments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssignmentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" assignment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AssignmentDTO> findOne(Long id);

    /**
     * Delete the "id" assignment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the assignment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AssignmentDTO> search(String query, Pageable pageable);
}
