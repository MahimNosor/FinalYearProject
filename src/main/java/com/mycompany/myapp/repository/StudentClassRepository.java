package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentClass;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StudentClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    @Query("SELECT COUNT(sc) FROM StudentClass sc JOIN sc.users u WHERE u.roles = 'ROLE_TEACHER' AND u.id = :teacherId")
    int countByTeacherId(@Param("teacherId") Long teacherId);

    List<StudentClass> findByUsers_Id(Long userId);
}
