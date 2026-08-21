package io.github.guilhermeclamego.arqonsearch.renderer;

import io.github.guilhermeclamego.arqonsearch.query.SearchQuery;

public interface SearchRenderer<T> {

    T render(SearchQuery query);
}
