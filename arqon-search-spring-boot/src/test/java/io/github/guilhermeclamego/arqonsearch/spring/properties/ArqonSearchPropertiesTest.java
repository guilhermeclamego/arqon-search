package io.github.guilhermeclamego.arqonsearch.spring.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ArqonSearchProperties Unit Tests")
class ArqonSearchPropertiesTest {

    @Nested
    @DisplayName("Default configuration tests")
    class DefaultConfiguration {

        @Test
        @DisplayName("given properties instance when created then enabled is true")
        void givenPropertiesInstance_whenCreated_thenEnabledIsTrue() {
            var properties = new ArqonSearchProperties();

            assertThat(properties.isEnabled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Enabled property tests")
    class EnabledProperty {

        @Test
        @DisplayName("given properties instance when set enabled to false then enabled is false")
        void givenPropertiesInstance_whenSetEnabledToFalse_thenEnabledIsFalse() {
            var properties = new ArqonSearchProperties();

            properties.setEnabled(false);

            assertThat(properties.isEnabled()).isFalse();
        }

        @Test
        @DisplayName("given disabled properties when set enabled to true then enabled is true")
        void givenDisabledProperties_whenSetEnabledToTrue_thenEnabledIsTrue() {
            var properties = new ArqonSearchProperties();
            properties.setEnabled(false);

            properties.setEnabled(true);

            assertThat(properties.isEnabled()).isTrue();
        }
    }
}