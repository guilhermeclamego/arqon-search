package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.TermClause;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AtlasTermRenderer Unit Tests")
class AtlasTermRendererTest {

    private AtlasTermRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new AtlasTermRenderer();
    }

    @Nested
    @DisplayName("Render equals operator tests")
    class RenderEqualsOperator {

        @Test
        @DisplayName("given valid term clause with string value when render then return correct equals bson document")
        void givenValidTermClauseWithStringValue_whenRender_thenReturnCorrectEqualsBsonDocument() {
            var field = new Field("category");
            var clause = new TermClause(field, "electronics");

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("equals");

            Document equalsBody = result.get("equals", Document.class);
            assertThat(equalsBody)
                    .isNotNull()
                    .containsEntry("path", "category")
                    .containsEntry("value", "electronics");
        }

        @Test
        @DisplayName("given valid term clause with boolean value when render then return correct equals bson document")
        void givenValidTermClauseWithBooleanValue_whenRender_thenReturnCorrectEqualsBsonDocument() {
            var field = new Field("active");
            var clause = new TermClause(field, true);

            Document result = renderer.render(clause);

            Document equalsBody = result.get("equals", Document.class);
            assertThat(equalsBody)
                    .isNotNull()
                    .containsEntry("path", "active")
                    .containsEntry("value", true);
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {
        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new TermClause(null, "value");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}