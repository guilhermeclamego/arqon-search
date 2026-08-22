package io.github.guilhermeclamego.arqonsearch.spring.configuration;

import io.github.guilhermeclamego.arqonsearch.spring.properties.ArqonSearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ArqonSearchProperties.class)
public class ArqonSearchConfiguration {
}