package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.repository.AssignmentRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Assignment} entity.
 */
public interface AssignmentSearchRepository extends ElasticsearchRepository<Assignment, Long>, AssignmentSearchRepositoryInternal {}

interface AssignmentSearchRepositoryInternal {
    Page<Assignment> search(String query, Pageable pageable);

    Page<Assignment> search(Query query);

    @Async
    void index(Assignment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AssignmentSearchRepositoryInternalImpl implements AssignmentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AssignmentRepository repository;

    AssignmentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AssignmentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Assignment> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Assignment> search(Query query) {
        SearchHits<Assignment> searchHits = elasticsearchTemplate.search(query, Assignment.class);
        List<Assignment> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Assignment entity) {
        repository.findOneWithEagerRelationships(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Assignment.class);
    }
}
