package io.github.guilhermeclamego.arqonsearch.spring.configuration;

import io.github.guilhermeclamego.arqonsearch.spring.properties.ArqonSearchProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArqonSearchConfiguration Integration Tests")
class ArqonSearchConfigurationTest {

    @Nested
    @DisplayName("Properties registration tests")
    class PropertiesRegistration {

        @Test
        @DisplayName("given configuration when context starts then properties bean is available")
        void givenConfiguration_whenContextStarts_thenPropertiesBeanIsAvailable() {
            try (var context = new AnnotationConfigApplicationContext()) {

                context.register(ArqonSearchConfiguration.class);
                context.refresh();

                var properties = context.getBean(ArqonSearchProperties.class);

                assertThat(properties).isNotNull();
                assertThat(properties.isEnabled()).isTrue();
            }
        }
    }
}