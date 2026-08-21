package io.github.guilhermeclamego.arqonsearch.atlas.renderer;

import io.github.guilhermeclamego.arqonsearch.query.SearchClause;
import org.bson.Document;

public interface AtlasClauseRenderer<T extends SearchClause> {

    Document render(T clause);
}
