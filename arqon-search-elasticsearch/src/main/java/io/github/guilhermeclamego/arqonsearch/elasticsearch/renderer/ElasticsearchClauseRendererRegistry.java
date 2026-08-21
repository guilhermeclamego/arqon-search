package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.*;

import java.util.HashMap;
import java.util.Map;

public final class ElasticsearchClauseRendererRegistry {

    private final Map<
            Class<? extends SearchClause>,
            ElasticsearchClauseRenderer<?>
            > renderers;

    public ElasticsearchClauseRendererRegistry() {

        this.renderers = new HashMap<>();

        register(TextClause.class, new ElasticsearchTextRenderer());
        register(TermClause.class, new ElasticsearchTermRenderer());
        register(FuzzyClause.class, new ElasticsearchFuzzyRenderer());
        register(
                AutocompleteClause.class,
                new ElasticsearchAutocompleteRenderer()
        );
        register(RangeClause.class, new ElasticsearchRangeRenderer());

        register(
                BooleanClause.class,
                new ElasticsearchBooleanRenderer(this)
        );
    }

    public <T extends SearchClause> void register(
            Class<T> type,
            ElasticsearchClauseRenderer<T> renderer
    ) {
        renderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public Query render(SearchClause clause) {

        ElasticsearchClauseRenderer<SearchClause> renderer =
                (ElasticsearchClauseRenderer<SearchClause>)
                        renderers.get(clause.getClass());

        if (renderer == null) {
            throw new IllegalArgumentException(
                    "No renderer registered for: "
                            + clause.getClass().getName()
            );
        }

        return renderer.render(clause);
    }
}