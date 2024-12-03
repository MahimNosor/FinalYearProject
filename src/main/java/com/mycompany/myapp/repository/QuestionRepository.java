package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.Question;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Query("SELECT q FROM Question q WHERE q.teacher.id = :teacherId")
    List<Question> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(q) FROM Question q WHERE q.teacher.id = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);
}
