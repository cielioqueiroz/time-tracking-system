package com.company.timetracking.domain.valueobjects;

import com.company.timetracking.domain.exceptions.InvalidEmailException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @Test
    void normalizesToTrimmedLowercase() {
        assertThat(Email.of("  Jose.Silva@Empresa.COM ").value())
                .isEqualTo("jose.silva@empresa.com");
    }

    @Test
    void rejectsBlankEmail() {
        assertThatThrownBy(() -> Email.of("  "))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void rejectsMalformedEmail() {
        assertThatThrownBy(() -> Email.of("not-an-email"))
                .isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void equalityIsCaseInsensitiveAfterNormalization() {
        assertThat(Email.of("A@B.com")).isEqualTo(Email.of("a@b.com"));
    }
}
