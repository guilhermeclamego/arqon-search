package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import org.bson.Document;

public final class AtlasAutocompleteRenderer
        implements AtlasClauseRenderer<AutocompleteClause> {

    @Override
    public Document render(AutocompleteClause clause) {
        return new Document(
                "autocomplete",
                new Document("query", clause.value())
                        .append("path", clause.field().name())
        );
    }
}