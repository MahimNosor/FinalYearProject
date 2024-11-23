package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.repository.StudentClassRepository;
import com.mycompany.myapp.repository.search.StudentClassSearchRepository;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import com.mycompany.myapp.service.mapper.StudentClassMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.StudentClass}.
 */
@Service
@Transactional
public class StudentClassService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentClassService.class);

    private final StudentClassRepository studentClassRepository;

    private final StudentClassMapper studentClassMapper;

    private final StudentClassSearchRepository studentClassSearchRepository;

    private final Logger log = LoggerFactory.getLogger(StudentClassService.class);

    public StudentClassService(
        StudentClassRepository studentClassRepository,
        StudentClassMapper studentClassMapper,
        StudentClassSearchRepository studentClassSearchRepository
    ) {
        this.studentClassRepository = studentClassRepository;
        this.studentClassMapper = studentClassMapper;
        this.studentClassSearchRepository = studentClassSearchRepository;
    }

    /**
     * Save a studentClass.
     *
     * @param studentClassDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentClassDTO save(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to save StudentClass : {}", studentClassDTO);
        StudentClass studentClass = studentClassMapper.toEntity(studentClassDTO);
        studentClass = studentClassRepository.save(studentClass);
        studentClassSearchRepository.index(studentClass);
        return studentClassMapper.toDto(studentClass);
    }

    /**
     * Update a studentClass.
     *
     * @param studentClassDTO the entity to save.
     * @return the persisted entity.
     */
    public StudentClassDTO update(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to update StudentClass : {}", studentClassDTO);
        StudentClass studentClass = studentClassMapper.toEntity(studentClassDTO);
        studentClass = studentClassRepository.save(studentClass);
        studentClassSearchRepository.index(studentClass);
        return studentClassMapper.toDto(studentClass);
    }

    /**
     * Partially update a studentClass.
     *
     * @param studentClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StudentClassDTO> partialUpdate(StudentClassDTO studentClassDTO) {
        LOG.debug("Request to partially update StudentClass : {}", studentClassDTO);

        return studentClassRepository
            .findById(studentClassDTO.getId())
            .map(existingStudentClass -> {
                studentClassMapper.partialUpdate(existingStudentClass, studentClassDTO);

                return existingStudentClass;
            })
            .map(studentClassRepository::save)
            .map(savedStudentClass -> {
                studentClassSearchRepository.index(savedStudentClass);
                return savedStudentClass;
            })
            .map(studentClassMapper::toDto);
    }

    /**
     * Get all the studentClasses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentClassDTO> findAll() {
        LOG.debug("Request to get all StudentClasses");
        return studentClassRepository.findAll().stream().map(studentClassMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one studentClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StudentClassDTO> findOne(Long id) {
        LOG.debug("Request to get StudentClass : {}", id);
        return studentClassRepository.findById(id).map(studentClassMapper::toDto);
    }

    /**
     * Delete the studentClass by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete StudentClass : {}", id);
        studentClassRepository.deleteById(id);
        studentClassSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the studentClass corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StudentClassDTO> search(String query) {
        LOG.debug("Request to search StudentClasses for query {}", query);
        try {
            return StreamSupport.stream(studentClassSearchRepository.search(query).spliterator(), false)
                .map(studentClassMapper::toDto)
                .toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<StudentClassDTO> findClassesByStudentId(Long studentId) {
        log.debug("Request to get classes for student ID: {}", studentId);
        return studentClassRepository.findByUsers_Id(studentId).stream().map(studentClassMapper::toDto).collect(Collectors.toList());
    }
}
