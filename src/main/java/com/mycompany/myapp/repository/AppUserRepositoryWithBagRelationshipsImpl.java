package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AppUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class AppUserRepositoryWithBagRelationshipsImpl implements AppUserRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String APPUSERS_PARAMETER = "appUsers";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<AppUser> fetchBagRelationships(Optional<AppUser> appUser) {
        return appUser.map(this::fetchClasses);
    }

    @Override
    public Page<AppUser> fetchBagRelationships(Page<AppUser> appUsers) {
        return new PageImpl<>(fetchBagRelationships(appUsers.getContent()), appUsers.getPageable(), appUsers.getTotalElements());
    }

    @Override
    public List<AppUser> fetchBagRelationships(List<AppUser> appUsers) {
        return Optional.of(appUsers).map(this::fetchClasses).orElse(Collections.emptyList());
    }

    AppUser fetchClasses(AppUser result) {
        return entityManager
            .createQuery("select appUser from AppUser appUser left join fetch appUser.classes where appUser.id = :id", AppUser.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<AppUser> fetchClasses(List<AppUser> appUsers) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, appUsers.size()).forEach(index -> order.put(appUsers.get(index).getId(), index));
        List<AppUser> result = entityManager
            .createQuery("select appUser from AppUser appUser left join fetch appUser.classes where appUser in :appUsers", AppUser.class)
            .setParameter(APPUSERS_PARAMETER, appUsers)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
