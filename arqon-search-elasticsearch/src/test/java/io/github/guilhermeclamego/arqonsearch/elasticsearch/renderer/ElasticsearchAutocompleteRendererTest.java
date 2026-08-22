package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchAutocompleteRenderer Unit Tests")
class ElasticsearchAutocompleteRendererTest {

    private ElasticsearchAutocompleteRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchAutocompleteRenderer();
    }

    @Nested
    @DisplayName("Render autocomplete operator tests")
    class RenderAutocompleteOperator {

        @Test
        @DisplayName("given valid autocomplete clause when render then return correct elasticsearch query structure")
        void givenValidAutocompleteClause_whenRender_thenReturnCorrectElasticsearchQueryStructure() {
            var field = new Field("title");
            var clause = new AutocompleteClause(field, "java search");

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isMatchPhrasePrefix()).isTrue();

            var matchPhrasePrefixQuery = result.matchPhrasePrefix();
            assertThat(matchPhrasePrefixQuery.field()).isEqualTo("title");
            assertThat(matchPhrasePrefixQuery.query()).isEqualTo("java search");
        }

        @Test
        @DisplayName("given clause with empty query string when render then return match phrase prefix query with empty string")
        void givenClauseWithEmptyQueryString_whenRender_thenReturnMatchPhrasePrefixQueryWithEmptyString() {
            var field = new Field("category");
            var clause = new AutocompleteClause(field, "");

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isMatchPhrasePrefix()).isTrue();

            var matchPhrasePrefixQuery = result.matchPhrasePrefix();
            assertThat(matchPhrasePrefixQuery.field()).isEqualTo("category");
            assertThat(matchPhrasePrefixQuery.query()).isEqualTo("");
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
            var clause = new AutocompleteClause(null, "search term");

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}