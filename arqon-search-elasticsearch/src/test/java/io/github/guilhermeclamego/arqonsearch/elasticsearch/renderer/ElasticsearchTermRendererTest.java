package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.TermClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchTermRenderer Unit Tests")
class ElasticsearchTermRendererTest {

    private ElasticsearchTermRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchTermRenderer();
    }

    @Nested
    @DisplayName("Render term operator tests")
    class RenderTermOperator {

        @Test
        @DisplayName("given valid term clause with string value when render then return correct elasticsearch term query")
        void givenValidTermClauseWithStringValue_whenRender_thenReturnCorrectElasticsearchTermQuery() {
            var field = new Field("status");
            var clause = new TermClause(field, "ACTIVE");

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isTerm()).isTrue();

            var termQuery = result.term();
            assertThat(termQuery.field()).isEqualTo("status");
            assertThat(termQuery.value().stringValue()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("given valid term clause with numeric value when render then return term query with string representation")
        void givenValidTermClauseWithNumericValue_whenRender_thenReturnTermQueryWithStringRepresentation() {
            var field = new Field("code");
            var clause = new TermClause(field, 200);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isTerm()).isTrue();

            var termQuery = result.term();
            assertThat(termQuery.field()).isEqualTo("code");
            assertThat(termQuery.value().stringValue()).isEqualTo("200");
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {

        @Test
        @DisplayName("given null clause when render then throw null pointer exception")
        void givenNullClause_whenRender_thenThrowNullPointerException() {
            assertThrows(NullPointerException.class, () -> renderer.render(null));
        }

        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new TermClause(null, "value");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}