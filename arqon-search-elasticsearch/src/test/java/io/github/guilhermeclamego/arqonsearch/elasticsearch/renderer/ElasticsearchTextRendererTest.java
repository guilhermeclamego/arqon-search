package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchTextRenderer Unit Tests")
class ElasticsearchTextRendererTest {

    private ElasticsearchTextRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchTextRenderer();
    }

    @Nested
    @DisplayName("Render text operator tests")
    class RenderTextOperator {

        @Test
        @DisplayName("given valid text clause when render then return correct elasticsearch match query")
        void givenValidTextClause_whenRender_thenReturnCorrectElasticsearchMatchQuery() {
            var field = new Field("description");
            var clause = new TextClause(field, "search query text");

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isMatch()).isTrue();

            var matchQuery = result.match();
            assertThat(matchQuery.field()).isEqualTo("description");
            assertThat(matchQuery.query().stringValue()).isEqualTo("search query text");
        }

        @Test
        @DisplayName("given text clause with empty query string when render then return match query with empty string")
        void givenTextClauseWithEmptyQueryString_whenRender_thenReturnMatchQueryWithEmptyString() {
            var field = new Field("title");
            var clause = new TextClause(field, "");

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isMatch()).isTrue();

            var matchQuery = result.match();
            assertThat(matchQuery.field()).isEqualTo("title");
            assertThat(matchQuery.query().stringValue()).isEqualTo("");
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
            var clause = new TextClause(null, "search text");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}