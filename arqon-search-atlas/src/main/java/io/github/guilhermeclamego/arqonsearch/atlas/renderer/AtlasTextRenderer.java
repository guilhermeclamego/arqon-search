package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.bson.Document;

public final class AtlasTextRenderer
        implements AtlasClauseRenderer<TextClause> {

    @Override
    public Document render(TextClause clause) {
        return new Document(
                "text",
                new Document("query", clause.value())
                        .append("path", clause.field().name())
        );
    }
}
