package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record TextClause(
        Field field,
        String value
) implements SearchClause {
}
