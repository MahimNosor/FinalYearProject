package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StudentClass;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mycompany.myapp.domain.AppUser;

/**
 * Spring Data JPA repository for the StudentClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {


    @Query("SELECT sc FROM StudentClass sc WHERE sc.appUser.id = :appUserId")
    List<StudentClass> findByAppUserId(@Param("appUserId") Long appUserId);

    List<StudentClass> findByUsers_Id(Long appUserId);

    @Query("SELECT COUNT(sc) FROM StudentClass sc WHERE sc.appUser.id = :teacherId")
    int countByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT COUNT(DISTINCT u.id) " +
        "FROM StudentClass sc " +
        "JOIN sc.users u " +
        "WHERE sc.appUser.id = :teacherId")
    int countTotalStudentsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT DISTINCT u FROM StudentClass sc JOIN sc.users u WHERE sc.appUser.id = :teacherId")
    List<AppUser> findDistinctStudentsByTeacherId(@Param("teacherId") Long teacherId);

}
