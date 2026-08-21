package io.github.guilhermeclamego.arqonsearch.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer.ElasticsearchClauseRendererRegistry;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.renderer.SearchRenderer;

import java.util.List;

public final class ElasticsearchSearchRenderer
        implements SearchRenderer<List<Query>> {

    private final ElasticsearchClauseRendererRegistry registry;

    public ElasticsearchSearchRenderer() {
        this.registry = new ElasticsearchClauseRendererRegistry();
    }

    @Override
    public List<Query> render(SearchQuery query) {
        return query.clauses()
                .stream()
                .map(registry::render)
                .toList();
    }
}