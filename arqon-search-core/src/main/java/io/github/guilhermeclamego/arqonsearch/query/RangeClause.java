package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.field.Field;

public record RangeClause(
        Field field,
        RangeOperator operator,
        Object value
) implements SearchClause {

    public static RangeClause gt(Field field, Object value) {
        return new RangeClause(field, RangeOperator.GT, value);
    }

    public static RangeClause gte(Field field, Object value) {
        return new RangeClause(field, RangeOperator.GTE, value);
    }

    public static RangeClause lt(Field field, Object value) {
        return new RangeClause(field, RangeOperator.LT, value);
    }

    public static RangeClause lte(Field field, Object value) {
        return new RangeClause(field, RangeOperator.LTE, value);
    }
}