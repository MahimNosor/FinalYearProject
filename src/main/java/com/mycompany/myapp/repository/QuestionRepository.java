package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT COUNT(q) FROM Question q JOIN q.studentClass sc JOIN sc.users u WHERE u.roles = 'ROLE_TEACHER' AND u.id = :teacherId")
    int countByTeacherId(@Param("teacherId") Long teacherId);
}
