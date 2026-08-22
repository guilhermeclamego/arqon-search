package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ElasticsearchClauseRendererRegistry Unit Tests")
class ElasticsearchClauseRendererRegistryTest {

    private ElasticsearchClauseRendererRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new ElasticsearchClauseRendererRegistry();
    }

    @Nested
    @DisplayName("Default registration tests")
    class DefaultRegistration {

        @Test
        @DisplayName("given default registry when render autocomplete clause then return correct elasticsearch query")
        void givenDefaultRegistry_whenRenderAutocompleteClause_thenReturnCorrectElasticsearchQuery() {
            var clause = new AutocompleteClause(new Field("title"), "java search");

            Query result = registry.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isMatchPhrasePrefix()).isTrue();
        }

        @Test
        @DisplayName("given default registry when render boolean clause then render nested clauses recursively")
        void givenDefaultRegistry_whenRenderBooleanClause_thenRenderNestedClausesRecursively() {
            var childClause = new AutocompleteClause(new Field("title"), "java");
            var booleanClause = new BooleanClause(BooleanOperator.MUST, List.of(childClause));

            Query result = registry.render(booleanClause);

            assertThat(result).isNotNull();
            assertThat(result.isBool()).isTrue();
        }
    }

    @Nested
    @DisplayName("Custom registration tests")
    class CustomRegistration {

        @Test
        @DisplayName("given custom renderer when register then overwrite existing renderer for clause type")
        void givenCustomRenderer_whenRegister_thenOverwriteExistingRendererForClauseType() {
            var clause = new AutocompleteClause(new Field("title"), "overridden");

            registry.register(AutocompleteClause.class, c -> new Query.Builder()
                    .term(t -> t.field(c.field().name()).value(c.value()))
                    .build());

            Query result = registry.render(clause);

            assertThat(result).isNotNull();
            assertThat(result.isTerm()).isTrue();
            assertThat(result.term().field()).isEqualTo("title");
            assertThat(result.term().value().stringValue()).isEqualTo("overridden");
        }
    }
}