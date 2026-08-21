package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.RangeClause;
import io.github.guilhermeclamego.arqonsearch.query.RangeOperator;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AtlasRangeRenderer Unit Tests")
class AtlasRangeRendererTest {

    private AtlasRangeRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new AtlasRangeRenderer();
    }

    @Nested
    @DisplayName("Render range operator tests")
    class RenderRangeOperator {

        @Test
        @DisplayName("given range clause with numeric value when render then return correct bson document")
        void givenRangeClauseWithNumericValue_whenRender_thenReturnCorrectBsonDocument() {
            var field = new Field("price");
            var clause = new RangeClause(field, RangeOperator.GTE, 100.0);

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("range");

            Document rangeBody = result.get("range", Document.class);
            assertThat(rangeBody)
                    .isNotNull()
                    .containsEntry("path", "price")
                    .containsEntry("gte", 100.0);
        }

        @Test
        @DisplayName("given range clause with date value when render then return correct bson document")
        void givenRangeClauseWithDateValue_whenRender_thenReturnCorrectBsonDocument() {
            var field = new Field("createdAt");
            var now = Instant.now();
            var clause = new RangeClause(field, RangeOperator.LT, now);

            Document result = renderer.render(clause);

            Document rangeBody = result.get("range", Document.class);
            assertThat(rangeBody)
                    .isNotNull()
                    .containsEntry("path", "createdAt")
                    .containsEntry("lt", now);
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {

        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new RangeClause(null, RangeOperator.GTE, 10);

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }

        @Test
        @DisplayName("given clause with null operator when render then throw null pointer exception")
        void givenClauseWithNullOperator_whenRender_thenThrowNullPointerException() {
            var field = new Field("price");
            var clause = new RangeClause(field, null, 10);

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}