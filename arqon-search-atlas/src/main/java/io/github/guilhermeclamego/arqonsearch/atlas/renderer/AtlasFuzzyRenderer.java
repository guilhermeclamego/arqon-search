package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.FuzzyClause;
import org.bson.Document;

public final class AtlasFuzzyRenderer
        implements AtlasClauseRenderer<FuzzyClause> {

    @Override
    public Document render(FuzzyClause clause) {
        Document fuzzy = new Document()
                .append("maxEdits", clause.maxEdits())
                .append("prefixLength", clause.prefixLength());

        return new Document(
                "text",
                new Document("query", clause.value())
                        .append("path", clause.field().name())
                        .append("fuzzy", fuzzy)
        );
    }
}