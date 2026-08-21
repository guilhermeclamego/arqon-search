package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record RangeClause(
        Field field,
        Object from,
        Object to
) implements SearchClause {
}
