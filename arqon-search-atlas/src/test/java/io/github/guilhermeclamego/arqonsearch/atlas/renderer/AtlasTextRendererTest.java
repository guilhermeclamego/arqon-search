package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AtlasTextRenderer Unit Tests")
class AtlasTextRendererTest {

    private AtlasTextRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new AtlasTextRenderer();
    }

    @Nested
    @DisplayName("Render text operator tests")
    class RenderTextOperator {

        @Test
        @DisplayName("given valid text clause when render then return correct bson document structure")
        void givenValidTextClause_whenRender_thenReturnCorrectBsonDocumentStructure() {
            var field = new Field("description");
            var clause = new TextClause(field, "developer Java");

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("text");

            Document textBody = result.get("text", Document.class);
            assertThat(textBody)
                    .isNotNull()
                    .containsEntry("query", "developer Java")
                    .containsEntry("path", "description");
        }

        @Test
        @DisplayName("given clause with empty string query when render then return document with empty query")
        void givenClauseWithEmptyStringQuery_whenRender_thenReturnDocumentWithEmptyQuery() {
            var field = new Field("title");
            var clause = new TextClause(field, "");

            Document result = renderer.render(clause);

            Document textBody = result.get("text", Document.class);
            assertThat(textBody)
                    .isNotNull()
                    .containsEntry("query", "")
                    .containsEntry("path", "title");
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {

        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new TextClause(null, "search term");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}