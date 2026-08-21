package io.github.guilhermeclamego.arqonsearch.query;

public sealed interface SearchClause
        permits TextClause,
        TermClause,
        FuzzyClause,
        AutocompleteClause,
        RangeClause,
        BooleanClause {
}
