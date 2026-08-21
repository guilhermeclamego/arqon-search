package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.TermClause;
import org.bson.Document;

public final class AtlasTermRenderer
        implements AtlasClauseRenderer<TermClause> {

    @Override
    public Document render(TermClause clause) {
        return new Document(
                "equals",
                new Document("path", clause.field().name())
                        .append("value", clause.value())
        );
    }
}
