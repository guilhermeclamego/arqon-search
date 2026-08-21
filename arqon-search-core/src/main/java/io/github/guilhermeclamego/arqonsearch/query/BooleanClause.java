package io.github.guilhermeclamego.arqonsearch.query;

import java.util.List;
import java.util.Objects;

public record BooleanClause(
        BooleanOperator operator,
        List<SearchClause> clauses
) implements SearchClause {

    public BooleanClause {
        Objects.requireNonNull(operator, "operator must not be null");
        Objects.requireNonNull(clauses, "clauses must not be null");

        if (clauses.isEmpty()) {
            throw new IllegalArgumentException("clauses must not be empty");
        }

        clauses = List.copyOf(clauses);
    }

    public static BooleanClause must(SearchClause... clauses) {
        return new BooleanClause(
                BooleanOperator.MUST,
                List.of(clauses)
        );
    }

    public static BooleanClause should(SearchClause... clauses) {
        return new BooleanClause(
                BooleanOperator.SHOULD,
                List.of(clauses)
        );
    }

    public static BooleanClause mustNot(SearchClause... clauses) {
        return new BooleanClause(
                BooleanOperator.MUST_NOT,
                List.of(clauses)
        );
    }

    public static BooleanClause filter(SearchClause... clauses) {
        return new BooleanClause(
                BooleanOperator.FILTER,
                List.of(clauses)
        );
    }
}