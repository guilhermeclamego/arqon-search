package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;

public final class ElasticsearchTextRenderer
        implements ElasticsearchClauseRenderer<TextClause> {

    @Override
    public Query render(TextClause clause) {
        return new Query.Builder()
                .match(match -> match
                        .field(clause.field().name())
                        .query(clause.value())
                )
                .build();
    }
}