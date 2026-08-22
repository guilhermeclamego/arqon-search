package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanOperator;
import io.github.guilhermeclamego.arqonsearch.query.SearchClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ElasticsearchBooleanRenderer Unit Tests")
class ElasticsearchBooleanRendererTest {

    private ElasticsearchClauseRendererRegistry registry;
    private ElasticsearchBooleanRenderer renderer;

    @BeforeEach
    void setUp() {
        registry = new ElasticsearchClauseRendererRegistry();
        renderer = new ElasticsearchBooleanRenderer(registry);
    }

    @Nested
    @DisplayName("Render boolean operator tests")
    class RenderBooleanOperator {

        @Test
        @DisplayName("given boolean clause with must operator when render then return correct elasticsearch bool query")
        void givenBooleanClauseWithMustOperator_whenRender_thenReturnCorrectElasticsearchBoolQuery() {
            SearchClause childClause1 = new AutocompleteClause(new Field("title"), "val1");
            SearchClause childClause2 = new AutocompleteClause(new Field("category"), "val2");

            registry.register(AutocompleteClause.class, clause ->
                    new Query.Builder()
                            .matchPhrasePrefix(m -> m.field(clause.field().name()).query(clause.value()))
                            .build()
            );

            var clause = new BooleanClause(BooleanOperator.MUST, List.of(childClause1, childClause2));

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isBool()).isTrue();

            var mustQueries = result.bool().must();
            assertThat(mustQueries).hasSize(2);
            assertThat(mustQueries.get(0).matchPhrasePrefix().field()).isEqualTo("title");
            assertThat(mustQueries.get(0).matchPhrasePrefix().query()).isEqualTo("val1");
            assertThat(mustQueries.get(1).matchPhrasePrefix().field()).isEqualTo("category");
            assertThat(mustQueries.get(1).matchPhrasePrefix().query()).isEqualTo("val2");
        }

        @Test
        @DisplayName("given boolean clause with should operator when render then return correct elasticsearch bool query")
        void givenBooleanClauseWithShouldOperator_whenRender_thenReturnCorrectElasticsearchBoolQuery() {
            SearchClause childClause = new AutocompleteClause(new Field("description"), "search term");

            registry.register(AutocompleteClause.class, clause ->
                    new Query.Builder()
                            .matchPhrasePrefix(m -> m.field(clause.field().name()).query(clause.value()))
                            .build()
            );

            var clause = new BooleanClause(BooleanOperator.SHOULD, List.of(childClause));

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isBool()).isTrue();

            var shouldQueries = result.bool().should();
            assertThat(shouldQueries).hasSize(1);
            assertThat(shouldQueries.getFirst().matchPhrasePrefix().field()).isEqualTo("description");
            assertThat(shouldQueries.getFirst().matchPhrasePrefix().query()).isEqualTo("search term");
        }

        @Test
        @DisplayName("given boolean clause with must_not operator when render then return correct elasticsearch bool query")
        void givenBooleanClauseWithMustNotOperator_whenRender_thenReturnCorrectElasticsearchBoolQuery() {
            SearchClause childClause = new AutocompleteClause(new Field("status"), "inactive");

            registry.register(AutocompleteClause.class, clause ->
                    new Query.Builder()
                            .matchPhrasePrefix(m -> m.field(clause.field().name()).query(clause.value()))
                            .build()
            );

            var clause = new BooleanClause(BooleanOperator.MUST_NOT, List.of(childClause));

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isBool()).isTrue();

            var mustNotQueries = result.bool().mustNot();
            assertThat(mustNotQueries).hasSize(1);
            assertThat(mustNotQueries.getFirst().matchPhrasePrefix().field()).isEqualTo("status");
            assertThat(mustNotQueries.getFirst().matchPhrasePrefix().query()).isEqualTo("inactive");
        }

        @Test
        @DisplayName("given boolean clause with filter operator when render then return correct elasticsearch bool query")
        void givenBooleanClauseWithFilterOperator_whenRender_thenReturnCorrectElasticsearchBoolQuery() {
            SearchClause childClause = new AutocompleteClause(new Field("active"), "true");

            registry.register(AutocompleteClause.class, clause ->
                    new Query.Builder()
                            .matchPhrasePrefix(m -> m.field(clause.field().name()).query(clause.value()))
                            .build()
            );

            var clause = new BooleanClause(BooleanOperator.FILTER, List.of(childClause));

            Query result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isBool()).isTrue();

            var filterQueries = result.bool().filter();
            assertThat(filterQueries).hasSize(1);
            assertThat(filterQueries.getFirst().matchPhrasePrefix().field()).isEqualTo("active");
            assertThat(filterQueries.getFirst().matchPhrasePrefix().query()).isEqualTo("true");
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