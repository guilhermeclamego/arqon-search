package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.*;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public final class AtlasClauseRendererRegistry {

    private final Map<
            Class<? extends SearchClause>,
            AtlasClauseRenderer<?>
            > renderers;

    public AtlasClauseRendererRegistry() {

        this.renderers = new HashMap<>();

        register(TextClause.class, new AtlasTextRenderer());
        register(TermClause.class, new AtlasTermRenderer());
        register(FuzzyClause.class, new AtlasFuzzyRenderer());
        register(AutocompleteClause.class, new AtlasAutocompleteRenderer());
        register(RangeClause.class, new AtlasRangeRenderer());

        register(
                BooleanClause.class,
                new AtlasBooleanRenderer(this)
        );
    }

    public <T extends SearchClause> void register(
            Class<T> type,
            AtlasClauseRenderer<T> renderer
    ) {
        renderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public Document render(SearchClause clause) {

        AtlasClauseRenderer<SearchClause> renderer =
                (AtlasClauseRenderer<SearchClause>)
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