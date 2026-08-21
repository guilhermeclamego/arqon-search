package io.github.guilhermeclamego.arqonsearch.atlas;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

class AtlasSearchRendererTest {

    @Test
    void shouldRenderTextQuery() {

        SearchQuery query = new SearchQuery(
                List.of(
                        new TextClause(
                                new Field("name"),
                                "Guilherme"
                        )
                ),
                List.of(),
                null
        );

        Document result = new AtlasSearchRenderer()
                .render(query);

        System.out.println(result.toJson());
    }
}