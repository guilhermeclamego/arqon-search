package io.github.guilhermeclamego.arqonsearch.query;

import io.github.guilhermeclamego.arqonsearch.pagination.Pagination;
import io.github.guilhermeclamego.arqonsearch.sort.Sort;

import java.util.List;

public record SearchQuery(
        List<SearchClause> clauses,
        List<Sort> sorts,
        Pagination pagination
) {
}
