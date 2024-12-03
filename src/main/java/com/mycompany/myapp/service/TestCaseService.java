package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.repository.TestCaseRepository;
import com.mycompany.myapp.repository.search.TestCaseSearchRepository;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import com.mycompany.myapp.service.mapper.TestCaseMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.TestCase}.
 */
@Service
@Transactional
public class TestCaseService {

    private static final Logger LOG = LoggerFactory.getLogger(TestCaseService.class);

    private final TestCaseRepository testCaseRepository;

    private final TestCaseMapper testCaseMapper;

    private final TestCaseSearchRepository testCaseSearchRepository;

    public TestCaseService(
        TestCaseRepository testCaseRepository,
        TestCaseMapper testCaseMapper,
        TestCaseSearchRepository testCaseSearchRepository
    ) {
        this.testCaseRepository = testCaseRepository;
        this.testCaseMapper = testCaseMapper;
        this.testCaseSearchRepository = testCaseSearchRepository;
    }

    /**
     * Save a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public TestCaseDTO save(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to save TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        testCaseSearchRepository.index(testCase);
        return testCaseMapper.toDto(testCase);
    }

    /**
     * Update a testCase.
     *
     * @param testCaseDTO the entity to save.
     * @return the persisted entity.
     */
    public TestCaseDTO update(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to update TestCase : {}", testCaseDTO);
        TestCase testCase = testCaseMapper.toEntity(testCaseDTO);
        testCase = testCaseRepository.save(testCase);
        testCaseSearchRepository.index(testCase);
        return testCaseMapper.toDto(testCase);
    }

    /**
     * Partially update a testCase.
     *
     * @param testCaseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestCaseDTO> partialUpdate(TestCaseDTO testCaseDTO) {
        LOG.debug("Request to partially update TestCase : {}", testCaseDTO);

        return testCaseRepository
            .findById(testCaseDTO.getId())
            .map(existingTestCase -> {
                testCaseMapper.partialUpdate(existingTestCase, testCaseDTO);

                return existingTestCase;
            })
            .map(testCaseRepository::save)
            .map(savedTestCase -> {
                testCaseSearchRepository.index(savedTestCase);
                return savedTestCase;
            })
            .map(testCaseMapper::toDto);
    }

    /**
     * Get all the testCases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> findAll() {
        LOG.debug("Request to get all TestCases");
        return testCaseRepository.findAll().stream().map(testCaseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one testCase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestCaseDTO> findOne(Long id) {
        LOG.debug("Request to get TestCase : {}", id);
        return testCaseRepository.findById(id).map(testCaseMapper::toDto);
    }

    /**
     * Delete the testCase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TestCase : {}", id);
        testCaseRepository.deleteById(id);
        testCaseSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the testCase corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TestCaseDTO> search(String query) {
        LOG.debug("Request to search TestCases for query {}", query);
        try {
            return StreamSupport.stream(testCaseSearchRepository.search(query).spliterator(), false).map(testCaseMapper::toDto).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
