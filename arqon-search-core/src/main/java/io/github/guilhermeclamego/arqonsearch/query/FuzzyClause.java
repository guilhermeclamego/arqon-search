package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record FuzzyClause(
        Field field,
        String value,
        int maxEdits,
        int prefixLength
) implements SearchClause {
}
