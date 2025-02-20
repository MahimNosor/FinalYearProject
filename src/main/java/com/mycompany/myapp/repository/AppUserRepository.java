package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AppUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppUser entity.
 *
 * When extending this class, extend AppUserRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AppUserRepository extends AppUserRepositoryWithBagRelationships, JpaRepository<AppUser, Long> {
    default Optional<AppUser> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AppUser> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AppUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @EntityGraph(attributePaths = "user")
    Optional<AppUser> findOneByUserId(Long userId);

    @Query("SELECT au FROM AppUser au JOIN au.user u WHERE u.login = :login")
    Optional<AppUser> findByUser_Login(@Param("login") String login);

    @Query("SELECT a FROM AppUser a ORDER BY a.points DESC")
    List<AppUser> findAllByOrderByPointsDesc();

    @Query("SELECT appUser FROM AppUser appUser LEFT JOIN FETCH appUser.classes WHERE appUser.user.id = :userId")
    Optional<AppUser> findOneByUserIdWithClasses(@Param("userId") Long userId);


    @Query(
    "SELECT au FROM AppUser au JOIN au.classes sc WHERE sc.id = :classId ORDER BY au.points DESC")
    List<AppUser> findByClassIdOrderByPointsDesc(@Param("classId") Long classId);
}
