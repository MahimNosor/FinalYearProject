package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.repository.AssignmentRepository;
import com.mycompany.myapp.repository.search.AssignmentSearchRepository;
import com.mycompany.myapp.service.criteria.AssignmentCriteria;
import com.mycompany.myapp.service.dto.AssignmentDTO;
import com.mycompany.myapp.service.mapper.AssignmentMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Assignment} entities in the database.
 * The main input is a {@link AssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentQueryService extends QueryService<Assignment> {

    private static final Logger LOG = LoggerFactory.getLogger(AssignmentQueryService.class);

    private final AssignmentRepository assignmentRepository;

    private final AssignmentMapper assignmentMapper;

    private final AssignmentSearchRepository assignmentSearchRepository;

    public AssignmentQueryService(
        AssignmentRepository assignmentRepository,
        AssignmentMapper assignmentMapper,
        AssignmentSearchRepository assignmentSearchRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentMapper = assignmentMapper;
        this.assignmentSearchRepository = assignmentSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssignmentDTO> findByCriteria(AssignmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.fetchBagRelationships(assignmentRepository.findAll(specification, page)).map(assignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssignmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Assignment> specification = createSpecification(criteria);
        return assignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Assignment> createSpecification(AssignmentCriteria criteria) {
        Specification<Assignment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Assignment_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Assignment_.title));
            }
            if (criteria.getDifficulty() != null) {
                specification = specification.and(buildSpecification(criteria.getDifficulty(), Assignment_.difficulty));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildSpecification(criteria.getLanguage(), Assignment_.language));
            }
            if (criteria.getMaxScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxScore(), Assignment_.maxScore));
            }
            if (criteria.getIsPreloaded() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPreloaded(), Assignment_.isPreloaded));
            }
            if (criteria.getAppUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAppUserId(), root -> root.join(Assignment_.appUser, JoinType.LEFT).get(AppUser_.id))
                );
            }
            if (criteria.getStudentClassesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStudentClassesId(), root ->
                        root.join(Assignment_.studentClasses, JoinType.LEFT).get(StudentClass_.id)
                    )
                );
            }
            if (criteria.getUserQuestionsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserQuestionsId(), root ->
                        root.join(Assignment_.userQuestions, JoinType.LEFT).get(UserQuestion_.id)
                    )
                );
            }
        }
        return specification;
    }
}
