package com.manager.coomanager.shared.domain.valueobjects;

import com.manager.coomanager.shared.domain.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StateRegistrationTest {

    @Test
    @DisplayName("Deve instanciar com sucesso uma Inscrição Estadual numérica válida")
    void shouldCreateValidNumericStateRegistration() {
        StateRegistration registration = new StateRegistration("123.456.789-00");
        assertThat(registration.getRegistrationNumber()).isEqualTo("12345678900");
    }

    @Test
    @DisplayName("Deve instanciar com sucesso quando a Inscrição Estadual for ISENTO")
    void shouldCreateExemptStateRegistration() {
        StateRegistration registration = new StateRegistration("Isento");
        assertThat(registration.getRegistrationNumber()).isEqualTo("ISENTO");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando a Inscrição Estadual for nula ou em branco")
    void shouldThrowExceptionWhenNullOrBlank() {
        assertThatThrownBy(() -> new StateRegistration(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("não pode ser nula ou vazia");

        assertThatThrownBy(() -> new StateRegistration("   "))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("não pode ser nula ou vazia");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando a Inscrição Estadual tiver tamanho inválido")
    void shouldThrowExceptionWhenInvalidLength() {
        assertThatThrownBy(() -> new StateRegistration("12345"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("deve conter entre 8 e 14 dígitos");

        assertThatThrownBy(() -> new StateRegistration("1234567890123456789"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("deve conter entre 8 e 14 dígitos");
    }
}

