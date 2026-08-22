package io.github.guilhermeclamego.arqonsearch.spring.autoconfigure;

import io.github.guilhermeclamego.arqonsearch.spring.configuration.ArqonSearchConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "arqon.search",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Import(ArqonSearchConfiguration.class)
public class ArqonSearchAutoConfiguration {
}