package com.pandolar.leadmanagement.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    @DisplayName("trimOrNull should trim whitespace")
    void trimOrNull_trimsWhitespace() {
        assertThat(StringUtils.trimOrNull("  hello  ")).isEqualTo("hello");
    }

    @Test
    @DisplayName("trimOrNull should return null for null input")
    void trimOrNull_returnsNullForNull() {
        assertThat(StringUtils.trimOrNull(null)).isNull();
    }

    @Test
    @DisplayName("normalizeEmail should trim and lowercase")
    void normalizeEmail_trimsAndLowercases() {
        assertThat(StringUtils.normalizeEmail("  JOHN@EXAMPLE.COM  ")).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("normalizeEmail should return null for null input")
    void normalizeEmail_returnsNullForNull() {
        assertThat(StringUtils.normalizeEmail(null)).isNull();
    }
}
