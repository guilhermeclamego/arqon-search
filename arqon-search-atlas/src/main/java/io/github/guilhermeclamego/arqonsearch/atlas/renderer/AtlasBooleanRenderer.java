package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import org.bson.Document;

import java.util.List;

public final class AtlasBooleanRenderer
        implements AtlasClauseRenderer<BooleanClause> {

    private final AtlasClauseRendererRegistry registry;

    public AtlasBooleanRenderer(AtlasClauseRendererRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Document render(BooleanClause clause) {

        List<Document> clauses = clause.clauses()
                .stream()
                .map(registry::render)
                .toList();

        return new Document(
                "compound",
                new Document(
                        clause.operator().name().toLowerCase(),
                        clauses
                )
        );
    }
}