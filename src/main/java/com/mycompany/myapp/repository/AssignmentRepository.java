package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Assignment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mycompany.myapp.domain.AppUser;


/**
 * Spring Data JPA repository for the Assignment entity.
 *
 * When extending this class, extend AssignmentRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AssignmentRepository extends AssignmentRepositoryWithBagRelationships, JpaRepository<Assignment, Long>, JpaSpecificationExecutor<Assignment> {
    default Optional<Assignment> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Assignment> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Assignment> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select assignment from Assignment assignment left join fetch assignment.appUser",
        countQuery = "select count(assignment) from Assignment assignment"
    )
    Page<Assignment> findAllWithToOneRelationships(Pageable pageable);

    @Query("select assignment from Assignment assignment left join fetch assignment.appUser")
    List<Assignment> findAllWithToOneRelationships();

    @Query("select assignment from Assignment assignment left join fetch assignment.appUser where assignment.id =:id")
    Optional<Assignment> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("SELECT a FROM Assignment a WHERE a.appUser.id = :appUserId")
    List<Assignment> findByAppUserId(@Param("appUserId") Long appUserId);

    @Query("SELECT a FROM Assignment a WHERE a.isPreloaded = true")
    List<Assignment> findPreloadedAssignments();

    List<Assignment> findByStudentClasses_Id(Long classId);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.appUser.id = :teacherId")
    int countByAppUserId(@Param("teacherId") Long teacherId);


}
