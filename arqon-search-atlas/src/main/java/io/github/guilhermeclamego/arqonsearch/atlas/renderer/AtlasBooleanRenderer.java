package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanOperator;
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
                        toAtlasOperator(clause.operator()),
                        clauses
                )
        );
    }

    private String toAtlasOperator(BooleanOperator operator) {
        return switch (operator) {
            case MUST -> "must";
            case SHOULD -> "should";
            case MUST_NOT -> "mustNot";
            case FILTER -> "filter";
        };
    }
}