package io.github.guilhermeclamego.arqonsearch.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;
import io.github.guilhermeclamego.arqonsearch.query.TermClause;
import io.github.guilhermeclamego.arqonsearch.query.TextClause;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ElasticsearchSearchRenderer Unit Tests")
class ElasticsearchSearchRendererTest {

    private ElasticsearchSearchRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new ElasticsearchSearchRenderer();
    }

    @Nested
    @DisplayName("Render search query tests")
    class RenderSearchQuery {

        @Test
        @DisplayName("given valid search query with multiple clauses when render then return list of elasticsearch queries")
        void givenValidSearchQueryWithMultipleClauses_whenRender_thenReturnListOfElasticsearchQueries() {
            var textClause = new TextClause(new Field("title"), "java search");
            var termClause = new TermClause(new Field("status"), "ACTIVE");
            var searchQuery = new SearchQuery(List.of(textClause, termClause), List.of(), null);

            List<Query> result = renderer.render(searchQuery);

            assertThat(result)
                    .isNotNull()
                    .hasSize(2);

            assertThat(result.getFirst().isMatch()).isTrue();
            assertThat(result.getFirst().match().field()).isEqualTo("title");
            assertThat(result.getFirst().match().query().stringValue()).isEqualTo("java search");

            assertThat(result.get(1).isTerm()).isTrue();
            assertThat(result.get(1).term().field()).isEqualTo("status");
            assertThat(result.get(1).term().value().stringValue()).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("given search query with empty clauses list when render then return empty list")
        void givenSearchQueryWithEmptyClausesList_whenRender_thenReturnEmptyList() {
            var searchQuery = new SearchQuery(List.of(), List.of(), null);

            List<Query> result = renderer.render(searchQuery);

            assertThat(result)
                    .isNotNull()
                    .isEmpty();
        }
    }
}