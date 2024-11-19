package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.TestCase;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {}
