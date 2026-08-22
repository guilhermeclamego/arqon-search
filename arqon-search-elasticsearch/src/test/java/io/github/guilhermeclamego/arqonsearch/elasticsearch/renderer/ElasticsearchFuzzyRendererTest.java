package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.FuzzyClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchFuzzyRenderer Unit Tests")
class ElasticsearchFuzzyRendererTest {

    private ElasticsearchFuzzyRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchFuzzyRenderer();
    }

    @Nested
    @DisplayName("Render fuzzy operator tests")
    class RenderFuzzyOperator {

        @Test
        @DisplayName("given valid fuzzy clause when render then return correct elasticsearch query structure")
        void givenValidFuzzyClause_whenRender_thenReturnCorrectElasticsearchQueryStructure() {
            var field = new Field("title");
            var clause = new FuzzyClause(field, "jva", 2, 0);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isFuzzy()).isTrue();

            var fuzzyQuery = result.fuzzy();
            assertThat(fuzzyQuery.field()).isEqualTo("title");
            assertThat(fuzzyQuery.value().stringValue()).isEqualTo("jva");
            assertThat(fuzzyQuery.fuzziness()).isEqualTo("2");
        }

        @Test
        @DisplayName("given fuzzy clause with zero max edits when render then return fuzzy query with zero fuzziness")
        void givenFuzzyClauseWithZeroMaxEdits_whenRender_thenReturnFuzzyQueryWithZeroFuzziness() {
            var field = new Field("author");
            var clause = new FuzzyClause(field, "exact", 0, 0);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isFuzzy()).isTrue();

            var fuzzyQuery = result.fuzzy();
            assertThat(fuzzyQuery.field()).isEqualTo("author");
            assertThat(fuzzyQuery.value().stringValue()).isEqualTo("exact");
            assertThat(fuzzyQuery.fuzziness()).isEqualTo("0");
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
            var clause = new FuzzyClause(null, "term", 1, 0);

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}