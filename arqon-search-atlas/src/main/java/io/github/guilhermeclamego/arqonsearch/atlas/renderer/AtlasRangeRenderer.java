package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.RangeClause;
import org.bson.Document;

public final class AtlasRangeRenderer
        implements AtlasClauseRenderer<RangeClause> {

    @Override
    public Document render(RangeClause clause) {
        return new Document(
                "range",
                new Document("path", clause.field().name())
                        .append(clause.operator().value(), clause.value())
        );
    }
}