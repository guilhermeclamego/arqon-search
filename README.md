# ArqonSearch 🎯

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Multi--Module-C71A36.svg)](https://maven.apache.org/)

**ArqonSearch** is a provider-agnostic Java search abstraction designed to build search queries once and translate them into native queries for different search engines.

The project aims to provide a common **Search DSL and AST** that can be translated into engine-specific representations such as **MongoDB Atlas Search** and **Elasticsearch**.

> 🚧 ArqonSearch is currently under active development.

---

## 💡 Motivation

Search implementations often become tightly coupled to the underlying search engine.

For example, an application using MongoDB Atlas Search may end up building BSON structures directly inside its domain or application layer:

```java
new Document("$search",
    new Document("text",
        new Document("query", "Java")
            .append("path", "title")
    )
);
```

Switching to Elasticsearch means rewriting the search logic using a completely different API and query model.
ArqonSearch aims to separate what the application wants to search for from how a specific search engine represents that query.

```text
                Application
                     │
                     ▼
              ArqonSearch Core
                     │
              Search AST / DSL
                     │
          ┌──────────┴──────────┐
          ▼                     ▼
   Atlas Search            Elasticsearch
          │                     │
          ▼                     ▼
     BSON / $search          JSON / DSL
```

The core abstraction knows nothing about MongoDB or Elasticsearch.

---

## 🎯 Goals

ArqonSearch is being designed around a few core principles:

* **Provider agnostic:** Search intent should not depend on a specific search engine.
* **Type safe:** Represent search operations using strongly typed Java objects.
* **Composable:** Queries should be composable into complex search expressions.
* **Immutable:** Query objects should be safe to reuse and compose.
* **Extensible:** Adding a new search provider should not require changes to the core.
* **Native translation:** Providers should generate native queries instead of executing a generic intermediate query at runtime.
* **Simple API:** The abstraction should hide engine-specific complexity without hiding search capabilities.

---

## 🏗️ Architecture

ArqonSearch is a multi-module Maven project.

```text
ArqonSearch/
│
├── arqon-search-core
│   └── Provider-agnostic search model and AST
│
├── arqon-search-atlas
│   └── MongoDB Atlas Search renderer
│
├── arqon-search-elasticsearch
│   └── Elasticsearch renderer
│
└── arqon-search-playground
    └── Local environment for experiments and integration tests
```

### Core
The `arqon-search-core` module contains the provider-independent search model.  
Examples:

* `SearchQuery`
* `SearchClause`
* `TextClause`
* `TermClause`
* `FuzzyClause`
* `AutocompleteClause`
* `RangeClause`
* `BooleanClause`
* `Sort`
* `Pagination`

The core module does not depend on MongoDB or Elasticsearch.

---

## 🌳 Search AST

The query is represented internally as a composable tree.  
For example:

```text
MUST
├── TEXT(name, "Guilherme")
├── RANGE(age >= 18)
└── SHOULD
    ├── TERM(status, "ACTIVE")
    └── TERM(status, "PENDING")
```

The same structure can then be translated by different providers.

```text
                    SearchQuery
                        │
                        ▼
                   Search AST
                        │
              ┌─────────┴─────────┐
              ▼                   ▼
        Atlas Renderer       Elasticsearch
              │                   │
              ▼                   ▼
        MongoDB BSON          Elasticsearch
                               Query DSL
```

This AST is the main abstraction boundary of the project.

---

## 🔌 Provider Architecture

Providers implement the translation from the ArqonSearch AST to their native query representation.

### MongoDB Atlas Search

```text
SearchClause
      │
      ▼
AtlasClauseRenderer
      │
      ▼
org.bson.Document
```

For example:

```java
TextClause(
    new Field("name"),
    "Guilherme"
)
```

can be translated into:

```json
{
  "text": {
    "query": "Guilherme",
    "path": "name"
  }
}
```

and ultimately composed into:

```json
{
  "$search": {
    "compound": {
      "must": [
        {
          "text": {
            "query": "Guilherme",
            "path": "name"
          }
        }
      ]
    }
  }
}
```

### Elasticsearch
The same ArqonSearch query will eventually be translated into the corresponding Elasticsearch Query DSL.  
The application should not need to know which provider is responsible for rendering the query.

---

## 📦 Modules

| Module | Responsibility |
| :--- | :--- |
| `arqon-search-core` | Search model, AST and provider-independent abstractions |
| `arqon-search-atlas` | MongoDB Atlas Search translation |
| `arqon-search-elasticsearch` | Elasticsearch translation |
| `arqon-search-playground` | Local development and integration environment |

---

## 🚧 Current Status

ArqonSearch is currently in the early development stage.

### Implemented
- [x] Multi-module Maven structure
- [x] Java 25
- [x] Provider-independent `SearchClause` model
- [x] `SearchQuery`
- [x] `TextClause`
- [x] `TermClause`
- [x] `FuzzyClause`
- [x] `AutocompleteClause`
- [x] `RangeClause`
- [x] `BooleanClause`
- [x] Atlas Search renderer architecture
- [x] Atlas text rendering
- [x] Atlas BSON generation
- [x] Initial JUnit tests

### Planned
- [ ] Complete Atlas Search operators
- [ ] Atlas compound query rendering
- [ ] Atlas sorting
- [ ] Atlas pagination
- [ ] Elasticsearch provider
- [ ] Fluent query DSL
- [ ] Comprehensive test suite
- [ ] Integration tests
- [ ] Documentation and examples
- [ ] Maven Central release

---

## 🧪 Testing

The project uses JUnit 5 for unit testing.  
Example:

```java
SearchQuery query = new SearchQuery(
    List.of(
        new TextClause(
            new Field("name"),
            "Guilherme"
        )
    ),
    List.of(),
    null
);

Document result = new AtlasSearchRenderer()
    .render(query);
```

The generated BSON can then be verified against the expected Atlas Search structure.

---

## 🚀 Roadmap

The project is being developed incrementally.

### Phase 1 — Core
Build a stable provider-independent search model.
```text
SearchQuery ──> SearchClause ──> AST
```

### Phase 2 — Atlas Search
Translate the AST into MongoDB Atlas Search BSON.
```text
AST ──> Atlas Renderer ──> BSON
```

### Phase 3 — Elasticsearch
Implement the same translation boundary for Elasticsearch.
```text
AST ──> Elasticsearch Renderer ──> Elasticsearch Query DSL
```

### Phase 4 — Fluent DSL
Provide a higher-level API for constructing queries:

```java
SearchQuery query = search()
    .must(text("name", "Guilherme"))
    .filter(range("age").gte(18))
    .should(term("status", "ACTIVE"))
    .build();
```

### Phase 5 — Production Readiness
* API stabilization
* Integration tests
* Documentation
* Performance benchmarks
* Maven Central publication

---

## 🤝 Contributing

ArqonSearch is currently an experimental/open-source project and contributions, ideas, and discussions are welcome.  
If you find a bug or have an idea for improving the abstraction, feel free to open an issue or pull request.

---

## 📄 License

This project is licensed under the MIT License.  
See the [LICENSE](LICENSE) file for details.