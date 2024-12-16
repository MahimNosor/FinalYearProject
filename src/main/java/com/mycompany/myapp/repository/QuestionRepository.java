package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Question;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.appUser.id = :appUserId")
    List<Question> findAllByAppUserId(@Param("appUserId") Long appUserId);
}
