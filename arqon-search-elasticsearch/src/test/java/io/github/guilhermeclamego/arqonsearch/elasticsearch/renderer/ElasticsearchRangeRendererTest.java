package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.RangeClause;
import io.github.guilhermeclamego.arqonsearch.query.RangeOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchRangeRenderer Unit Tests")
class ElasticsearchRangeRendererTest {

    private ElasticsearchRangeRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchRangeRenderer();
    }

    @Nested
    @DisplayName("Render range operator tests")
    class RenderRangeOperator {

        @Test
        @DisplayName("given range clause with GT operator when render then return correct elasticsearch range query")
        void givenRangeClauseWithGtOperator_whenRender_thenReturnCorrectElasticsearchRangeQuery() {
            var field = new Field("price");
            var clause = new RangeClause(field, RangeOperator.GT, 100);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isRange()).isTrue();

            var rangeQuery = result.range().number();
            assertThat(rangeQuery.field()).isEqualTo("price");
            assertThat(rangeQuery.gt()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("given range clause with GTE operator when render then return correct elasticsearch range query")
        void givenRangeClauseWithGteOperator_whenRender_thenReturnCorrectElasticsearchRangeQuery() {
            var field = new Field("age");
            var clause = new RangeClause(field, RangeOperator.GTE, 18);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isRange()).isTrue();

            var rangeQuery = result.range().number();
            assertThat(rangeQuery.field()).isEqualTo("age");
            assertThat(rangeQuery.gte()).isEqualTo(18.0);
        }

        @Test
        @DisplayName("given range clause with LT operator when render then return correct elasticsearch range query")
        void givenRangeClauseWithLtOperator_whenRender_thenReturnCorrectElasticsearchRangeQuery() {
            var field = new Field("stock");
            var clause = new RangeClause(field, RangeOperator.LT, 5);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isRange()).isTrue();

            var rangeQuery = result.range().number();
            assertThat(rangeQuery.field()).isEqualTo("stock");
            assertThat(rangeQuery.lt()).isEqualTo(5.0);
        }

        @Test
        @DisplayName("given range clause with LTE operator when render then return correct elasticsearch range query")
        void givenRangeClauseWithLteOperator_whenRender_thenReturnCorrectElasticsearchRangeQuery() {
            var field = new Field("discount");
            var clause = new RangeClause(field, RangeOperator.LTE, 50.5);

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isRange()).isTrue();

            var rangeQuery = result.range().number();
            assertThat(rangeQuery.field()).isEqualTo("discount");
            assertThat(rangeQuery.lte()).isEqualTo(50.5);
        }
    }

    @Nested
    @DisplayName("Edge cases and null safety tests")
    class EdgeCasesAndNullSafety {

        @Test
        @DisplayName("given clause with null field when render then throw null pointer exception")
        void givenClauseWithNullField_whenRender_thenThrowNullPointerException() {
            var clause = new RangeClause(null, RangeOperator.GT, 10);

            assertThrows(NullPointerException.class, () -> renderer.render(clause));
        }
    }
}