package io.github.guilhermeclamego.arqonsearch.sort;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record Sort(
        Field field,
        SortDirection direction
) {
}