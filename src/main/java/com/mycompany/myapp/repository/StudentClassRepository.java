package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {}
