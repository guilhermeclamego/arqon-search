package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.AutocompleteClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanClause;
import io.github.guilhermeclamego.arqonsearch.query.BooleanOperator;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AtlasClauseRendererRegistry Unit Tests")
class AtlasClauseRendererRegistryTest {

    private AtlasClauseRendererRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new AtlasClauseRendererRegistry();
    }

    @Nested
    @DisplayName("Default registration tests")
    class DefaultRegistration {

        @Test
        @DisplayName("given default registry when render autocomplete clause then return correct bson document")
        void givenDefaultRegistry_whenRenderAutocompleteClause_thenReturnCorrectBsonDocument() {
            var clause = new AutocompleteClause(new Field("title"), "java search");

            Document result = registry.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("autocomplete");
        }

        @Test
        @DisplayName("given default registry when render boolean clause then render nested clauses recursively")
        void givenDefaultRegistry_whenRenderBooleanClause_thenRenderNestedClausesRecursively() {
            var childClause = new AutocompleteClause(new Field("title"), "java");
            var booleanClause = new BooleanClause(BooleanOperator.MUST, List.of(childClause));

            Document result = registry.render(booleanClause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("compound");
        }
    }

    @Nested
    @DisplayName("Custom registration tests")
    class CustomRegistration {

        @Test
        @DisplayName("given custom renderer when register then overwrite existing renderer for clause type")
        void givenCustomRenderer_whenRegister_thenOverwriteExistingRendererForClauseType() {
            var clause = new AutocompleteClause(new Field("title"), "overridden");

            registry.register(AutocompleteClause.class, c -> new Document("custom_autocomplete", c.value()));

            Document result = registry.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("custom_autocomplete");
            assertThat(result.getString("custom_autocomplete")).isEqualTo("overridden");
        }
    }
}