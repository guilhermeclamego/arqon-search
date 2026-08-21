# ArqonSearch 🎯

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://www.oracle.com/java/)
[![Maven Central](https://img.shields.io/badge/Maven--Central-v1.0.0--SNAPSHOT-green.svg)](#)

**ArqonSearch** is a lightweight, provider-agnostic Java library designed to unify full-text search across **MongoDB Atlas Search** and **Elasticsearch**.

Born from real-world enterprise constraints, ArqonSearch eliminates the complexity of manually assembling verbose BSON/JSON aggregation pipelines or tightly coupling domain models to database-specific search drivers. It offers an intuitive, type-safe **Fluent DSL** that allows you to define search queries once and translate them natively into optimized queries for your target engine.

---

## 🔑 Key Features

* **Fluent Query DSL:** Express complex full-text search queries (`text`, `fuzzy`, `wildcard`, `autocomplete`, and `compound` boolean logic) using clean Java code.
* **Decoupled Architecture:** The `arqon-search-core` module has **zero dependencies** on MongoDB or Elasticsearch drivers, keeping your domain layer clean.
* **Atlas Search Engine (`arqon-search-atlas`):** Converts core search objects into native MongoDB `$search` BSON aggregation pipelines.
* **Elasticsearch Engine (`arqon-search-elasticsearch`):** Translates the same core models into native Elasticsearch Java API Client queries.
* **Pagination & Relevancy Built-In:** First-class support for page-based offsets (`$skip`/`$limit`), sorting, and score boosting.

---

## 🏗️ Architecture & Modules

The repository is structured as a multi-module Maven project:

```text
ArqonSearch/ (Root Monorepo)
├── arqon-search-core           ← Neutral domain models, operators, and Query Builders
├── arqon-search-atlas          ← Translator module for MongoDB Atlas Search ($search)
├── arqon-search-elasticsearch  ← Translator module for Elasticsearch
└── arqon-search-playground     ← Standalone execution environment for testing
```

---

## 📦 Installation

Add the core dependency and your desired search provider module to your `pom.xml`:

```xml
<dependencies>
    <!-- Core Search Abstraction -->
    <dependency>
        <groupId>io.github.guilhermeclamego</groupId>
        <artifactId>arqon-search-core</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>

    <!-- Atlas Search Provider (for MongoDB) -->
    <dependency>
        <groupId>io.github.guilhermeclamego</groupId>
        <artifactId>arqon-search-atlas</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

---

## 🚀 Quickstart Usage

### 1. Build a Unified Search Query (core)
Define your search intent using the neutral `SearchQuery` builder:

```java
import io.github.guilhermeclamego.arqon.core.model.SearchClause;
import io.github.guilhermeclamego.arqon.core.model.SearchQuery;

SearchQuery query = SearchQuery.index("default_index")
        .must(SearchClause.text("title", "Java"))
        .must(SearchClause.fuzzy("description", "Spring", 2))
        .should(SearchClause.wildcard("category", "Tech*"))
        .page(0, 10);
```

### 2. Execute on MongoDB Atlas Search (atlas)
Translate the neutral query into a MongoDB `$search` BSON aggregation pipeline and run it directly using the official Java driver:

```java
import io.github.guilhermeclamego.arqon.atlas.AtlasSearchTranslator;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.List;
import java.util.ArrayList;

// Convert core query to Atlas BSON pipeline
List<Bson> pipeline = AtlasSearchTranslator.toPipeline(query);

// Execute directly on your MongoCollection
MongoCollection<Document> collection = database.getCollection("products");
List<Document> results = collection.aggregate(pipeline).into(new ArrayList<>());
```

---

## 🛠️ Local Playground Setup

Want to test ArqonSearch locally with real database instances? The `arqon-search-playground` module includes a complete `docker-compose` stack running MongoDB (with ReplicaSet enabled for Atlas Search) and Elasticsearch + Kibana.

```bash
# 1. Clone the repository
git clone https://github.com/guilhermeclamego/ArqonSearch.git
cd ArqonSearch

# 2. Start local MongoDB & Elasticsearch containers
docker compose up -d

# 3. Run the Playground Application
mvn clean install
```

---

## 📄 Article & Background

To understand the motivation and technical background behind this project, check out the original article published on Medium:

[Usando Atlas Search com Java](https://medium.com/)

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
