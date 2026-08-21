package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.field.Field;
import io.github.guilhermeclamego.arqonsearch.query.FuzzyClause;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AtlasFuzzyRenderer Unit Tests")
class AtlasFuzzyRendererTest {

    private AtlasFuzzyRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new AtlasFuzzyRenderer();
    }

    @Nested
    @DisplayName("Render fuzzy operator tests")
    class RenderFuzzyOperator {

        @Test
        @DisplayName("given valid fuzzy clause when render then return correct bson document structure")
        void givenValidFuzzyClause_whenRender_thenReturnCorrectBsonDocumentStructure() {
            var field = new Field("title");
            var clause = new FuzzyClause(field, "jav", 2, 1);

            Document result = renderer.render(clause);

            assertThat(result).isNotNull();
            assertThat(result).containsKey("text");

            Document textBody = result.get("text", Document.class);
            assertThat(textBody)
                    .isNotNull()
                    .containsEntry("query", "jav")
                    .containsEntry("path", "title")
                    .containsKey("fuzzy");

            Document fuzzyBody = textBody.get("fuzzy", Document.class);
            assertThat(fuzzyBody)
                    .isNotNull()
                    .containsEntry("maxEdits", 2)
                    .containsEntry("prefixLength", 1);
        }

        @Test
        @DisplayName("given fuzzy clause with zero prefix length when render then return document with zero prefix length")
        void givenFuzzyClauseWithZeroPrefixLength_whenRender_thenReturnDocumentWithZeroPrefixLength() {
            var field = new Field("description");
            var clause = new FuzzyClause(field, "search", 1, 0);

            Document result = renderer.render(clause);

            Document textBody = result.get("text", Document.class);
            Document fuzzyBody = textBody.get("fuzzy", Document.class);

            assertThat(fuzzyBody)
                    .isNotNull()
                    .containsEntry("maxEdits", 1)
                    .containsEntry("prefixLength", 0);
        }
    }
}