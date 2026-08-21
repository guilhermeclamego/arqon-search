package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;

public final class ElasticsearchAutocompleteRenderer
        implements ElasticsearchClauseRenderer<AutocompleteClause> {

    @Override
    public Query render(AutocompleteClause clause) {
        return new Query.Builder()
                .matchPhrasePrefix(match -> match
                        .field(clause.field().name())
                        .query(clause.value())
                )
                .build();
    }
}