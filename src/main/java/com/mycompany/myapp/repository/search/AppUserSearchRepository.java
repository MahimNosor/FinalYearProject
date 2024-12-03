package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.repository.AppUserRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link AppUser} entity.
 */
public interface AppUserSearchRepository extends ElasticsearchRepository<AppUser, Long>, AppUserSearchRepositoryInternal {}

interface AppUserSearchRepositoryInternal {
    Stream<AppUser> search(String query);

    Stream<AppUser> search(Query query);

    @Async
    void index(AppUser entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AppUserSearchRepositoryInternalImpl implements AppUserSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AppUserRepository repository;

    AppUserSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AppUserRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<AppUser> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<AppUser> search(Query query) {
        return elasticsearchTemplate.search(query, AppUser.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(AppUser entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AppUser.class);
    }
}
