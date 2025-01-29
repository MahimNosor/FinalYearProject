package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserQuestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the UserQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    @Query("SELECT uq FROM UserQuestion uq WHERE uq.appUser.id = :appUserId ORDER BY uq.submissionDate DESC")
    List<UserQuestion> findByAppUserIdOrderBySubmissionDateDesc(@Param("appUserId") Long appUserId);

}
