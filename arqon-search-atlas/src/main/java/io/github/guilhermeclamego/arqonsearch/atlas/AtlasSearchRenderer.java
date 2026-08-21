package io.github.guilhermeclamego.arqonsearch.atlas;

import io.github.guilhermeclamego.arqonsearch.atlas.renderer.AtlasClauseRendererRegistry;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.renderer.SearchRenderer;
import org.bson.Document;

import java.util.List;

public final class AtlasSearchRenderer
        implements SearchRenderer<Document> {

    private final AtlasClauseRendererRegistry registry;

    public AtlasSearchRenderer() {
        this.registry = new AtlasClauseRendererRegistry();
    }

    @Override
    public Document render(SearchQuery query) {

        List<Document> clauses = query.clauses()
                .stream()
                .map(registry::render)
                .toList();

        return new Document(
                "$search",
                new Document(
                        "compound",
                        new Document("must", clauses)
                )
        );
    }
}