package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.repository.TestCaseRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link TestCase} entity.
 */
public interface TestCaseSearchRepository extends ElasticsearchRepository<TestCase, Long>, TestCaseSearchRepositoryInternal {}

interface TestCaseSearchRepositoryInternal {
    Stream<TestCase> search(String query);

    Stream<TestCase> search(Query query);

    @Async
    void index(TestCase entity);

    @Async
    void deleteFromIndexById(Long id);
}

class TestCaseSearchRepositoryInternalImpl implements TestCaseSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final TestCaseRepository repository;

    TestCaseSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, TestCaseRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<TestCase> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<TestCase> search(Query query) {
        return elasticsearchTemplate.search(query, TestCase.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(TestCase entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), TestCase.class);
    }
}
