package io.github.guilhermeclamego.arqonsearch.query;

public enum RangeOperator {

    GT("gt"),
    GTE("gte"),
    LT("lt"),
    LTE("lte");

    private final String value;

    RangeOperator(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}