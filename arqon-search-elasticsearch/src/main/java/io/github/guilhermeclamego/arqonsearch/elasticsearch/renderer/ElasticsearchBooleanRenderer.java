package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;

import java.util.List;

public final class ElasticsearchBooleanRenderer
        implements ElasticsearchClauseRenderer<BooleanClause> {

    private final ElasticsearchClauseRendererRegistry registry;

    public ElasticsearchBooleanRenderer(
            ElasticsearchClauseRendererRegistry registry
    ) {
        this.registry = registry;
    }

    @Override
    public Query render(BooleanClause clause) {

        List<Query> queries = clause.clauses()
                .stream()
                .map(registry::render)
                .toList();

        return new Query.Builder()
                .bool(bool -> {
                    switch (clause.operator()) {
                        case MUST -> bool.must(queries);
                        case SHOULD -> bool.should(queries);
                        case MUST_NOT -> bool.mustNot(queries);
                        case FILTER -> bool.filter(queries);
                    }

                    return bool;
                })
                .build();
    }
}