package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AtlasAutocompleteRenderer Unit Tests")
class AtlasAutocompleteRendererTest {

    private AtlasAutocompleteRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new AtlasAutocompleteRenderer();
    }

    @Nested
    @DisplayName("Render autocomplete operator tests")
    class RenderAutocompleteOperator {

        @Test
        @DisplayName("given valid autocomplete clause when render then return correct bson document structure")
        void givenValidAutocompleteClause_whenRender_thenReturnCorrectBsonDocumentStructure() {
            var field = new Field("title");
            var clause = new AutocompleteClause(field, "java search");

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("autocomplete");

            Document autocompleteBody = result.get("autocomplete", Document.class);
            assertThat(autocompleteBody)
                    .isNotNull()
                    .containsEntry("query", "java search")
                    .containsEntry("path", "title");
        }

        @Test
        @DisplayName("given clause with empty query string when render then return document with empty query")
        void givenClauseWithEmptyQueryString_whenRender_thenReturnDocumentWithEmptyQuery() {
            var field = new Field("category");
            var clause = new AutocompleteClause(field, "");

            Document result = renderer.render(clause);

            Document autocompleteBody = result.get("autocomplete", Document.class);
            assertThat(autocompleteBody)
                    .isNotNull()
                    .containsEntry("query", "")
                    .containsEntry("path", "category");
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {

        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new AutocompleteClause(null, "search term");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}