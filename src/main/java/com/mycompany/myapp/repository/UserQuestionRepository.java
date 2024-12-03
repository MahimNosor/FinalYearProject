package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {}
