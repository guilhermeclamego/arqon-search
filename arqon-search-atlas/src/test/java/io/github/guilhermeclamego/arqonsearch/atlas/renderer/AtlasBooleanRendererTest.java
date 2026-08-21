package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanOperator;
import io.github.guilhermeclamego.arqonsearch.query.SearchClause;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AtlasBooleanRenderer Unit Tests")
class AtlasBooleanRendererTest {

    private AtlasClauseRendererRegistry registry;
    private AtlasBooleanRenderer renderer;

    @BeforeEach
    void setUp() {
        registry = new AtlasClauseRendererRegistry();
        renderer = new AtlasBooleanRenderer(registry);
    }

    @Nested
    @DisplayName("Render compound operator tests")
    class RenderCompoundOperator {

        @Test
        @DisplayName("given boolean clause with must operator when render then return correct compound bson document")
        void givenBooleanClauseWithMustOperator_whenRender_thenReturnCorrectCompoundBsonDocument() {
            SearchClause childClause1 = new AutocompleteClause(new Field("title"), "val1");
            SearchClause childClause2 = new AutocompleteClause(new Field("category"), "val2");

            registry.register(AutocompleteClause.class, clause ->
                    new Document("autocomplete", new Document("query", clause.value())));

            var clause = new BooleanClause(BooleanOperator.MUST, List.of(childClause1, childClause2));

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("compound");

            Document compoundBody = result.get("compound", Document.class);
            assertThat(compoundBody).isNotNull().containsKey("must");

            @SuppressWarnings("unchecked")
            List<Document> mustClauses = (List<Document>) compoundBody.get("must");
            assertThat(mustClauses)
                    .hasSize(2)
                    .containsExactly(
                            new Document("autocomplete", new Document("query", "val1")),
                            new Document("autocomplete", new Document("query", "val2"))
                    );
        }

        @Test
        @DisplayName("given boolean clause with should operator when render then return correct compound bson document")
        void givenBooleanClauseWithShouldOperator_whenRender_thenReturnCorrectCompoundBsonDocument() {
            SearchClause childClause = new AutocompleteClause(new Field("description"), "search term");

            registry.register(AutocompleteClause.class, clause ->
                    new Document("autocomplete", new Document("query", clause.value())));

            var clause = new BooleanClause(BooleanOperator.SHOULD, List.of(childClause));

            Document result = renderer.render(clause);

            Document compoundBody = result.get("compound", Document.class);
            assertThat(compoundBody).isNotNull().containsKey("should");

            @SuppressWarnings("unchecked")
            List<Document> shouldClauses = (List<Document>) compoundBody.get("should");
            assertThat(shouldClauses)
                    .hasSize(1)
                    .containsExactly(new Document("autocomplete", new Document("query", "search term")));
        }

        @Test
        @DisplayName("given empty clauses list when instantiate boolean clause then throw illegal argument exception")
        void givenEmptyClausesList_whenInstantiateBooleanClause_thenThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new BooleanClause(BooleanOperator.MUST, List.of())
            );
        }
    }
}