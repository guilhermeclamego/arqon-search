package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.TermClause;

public final class ElasticsearchTermRenderer
        implements ElasticsearchClauseRenderer<TermClause> {

    @Override
    public Query render(TermClause clause) {
        return new Query.Builder()
                .term(term -> term
                        .field(clause.field().name())
                        .value(clause.value().toString())
                )
                .build();
    }
}