package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record TermClause(
        Field field,
        Object value
) implements SearchClause {
}
