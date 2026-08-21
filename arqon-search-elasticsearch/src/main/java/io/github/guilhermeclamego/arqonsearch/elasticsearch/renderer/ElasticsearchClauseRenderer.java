package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.SearchClause;

public interface ElasticsearchClauseRenderer<T extends SearchClause> {

    Query render(T clause);
}