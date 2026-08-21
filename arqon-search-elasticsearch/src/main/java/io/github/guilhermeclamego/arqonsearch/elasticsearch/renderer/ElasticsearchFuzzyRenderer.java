package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.FuzzyClause;

public final class ElasticsearchFuzzyRenderer
        implements ElasticsearchClauseRenderer<FuzzyClause> {

    @Override
    public Query render(FuzzyClause clause) {
        return new Query.Builder()
                .fuzzy(fuzzy -> fuzzy
                        .field(clause.field().name())
                        .value(clause.value())
                        .fuzziness(String.valueOf(clause.maxEdits()))
                )
                .build();
    }
}