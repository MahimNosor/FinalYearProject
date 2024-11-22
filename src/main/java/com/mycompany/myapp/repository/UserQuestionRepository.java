package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserQuestion;
import com.mycompany.myapp.domain.enumeration.SubmissionStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
    @Query(
        "SELECT COUNT(uq) FROM UserQuestion uq JOIN uq.question q JOIN q.studentClass sc JOIN sc.users u WHERE u.roles = 'ROLE_TEACHER' AND u.id = :teacherId AND uq.status = :status"
    )
    int countByTeacherIdAndStatus(@Param("teacherId") Long teacherId, @Param("status") SubmissionStatus status);
}
