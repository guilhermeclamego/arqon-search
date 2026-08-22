package io.github.guilhermeclamego.arqonsearch.spring.autoconfigure;

import io.github.guilhermeclamego.arqonsearch.spring.properties.ArqonSearchProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArqonSearchAutoConfiguration Tests")
class ArqonSearchAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(ArqonSearchAutoConfiguration.class);

    @Nested
    @DisplayName("Default configuration tests")
    class DefaultConfiguration {

        @Test
        @DisplayName("given no properties when context starts then ArqonSearch is enabled")
        void givenNoProperties_whenContextStarts_thenArqonSearchIsEnabled() {
            contextRunner.run(context -> {
                assertThat(context)
                        .hasNotFailed();

                assertThat(context)
                        .hasSingleBean(ArqonSearchProperties.class);

                var properties = context.getBean(ArqonSearchProperties.class);

                assertThat(properties.isEnabled()).isTrue();
            });
        }
    }

    @Nested
    @DisplayName("Enabled property tests")
    class EnabledProperty {

        @Test
        @DisplayName("given enabled property true when context starts then ArqonSearch is enabled")
        void givenEnabledPropertyTrue_whenContextStarts_thenArqonSearchIsEnabled() {
            contextRunner
                    .withPropertyValues("arqon.search.enabled=true")
                    .run(context -> {
                        assertThat(context)
                                .hasNotFailed();

                        assertThat(context)
                                .hasSingleBean(ArqonSearchProperties.class);

                        var properties =
                                context.getBean(ArqonSearchProperties.class);

                        assertThat(properties.isEnabled()).isTrue();
                    });
        }

        @Test
        @DisplayName("given enabled property false when context starts then ArqonSearch is disabled")
        void givenEnabledPropertyFalse_whenContextStarts_thenArqonSearchIsDisabled() {
            contextRunner
                    .withPropertyValues("arqon.search.enabled=false")
                    .run(context -> {
                        assertThat(context)
                                .hasNotFailed();

                        assertThat(context)
                                .doesNotHaveBean(ArqonSearchProperties.class);
                    });
        }
    }
}