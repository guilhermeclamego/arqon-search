package io.github.guilhermeclamego.arqonsearch.elasticsearch.renderer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import io.github.guilhermeclamego.arqonsearch.query.RangeClause;

public final class ElasticsearchRangeRenderer
        implements ElasticsearchClauseRenderer<RangeClause> {

    @Override
    public Query render(RangeClause clause) {

        String value = clause.value().toString();

        return switch (clause.operator()) {
            case GT -> new Query.Builder()
                    .range(range -> range
                            .number(number -> number
                                    .field(clause.field().name())
                                    .gt(Double.valueOf(value))
                            )
                    )
                    .build();

            case GTE -> new Query.Builder()
                    .range(range -> range
                            .number(number -> number
                                    .field(clause.field().name())
                                    .gte(Double.valueOf(value))
                            )
                    )
                    .build();

            case LT -> new Query.Builder()
                    .range(range -> range
                            .number(number -> number
                                    .field(clause.field().name())
                                    .lt(Double.valueOf(value))
                            )
                    )
                    .build();

            case LTE -> new Query.Builder()
                    .range(range -> range
                            .number(number -> number
                                    .field(clause.field().name())
                                    .lte(Double.valueOf(value))
                            )
                    )
                    .build();
        };
    }
}