package com.manager.coopafi.domain.valueObjects;

import com.manager.coopafi.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Embeddable
public final class BirthDate {

    @Column(name = "birth_date")
    private final LocalDate date;
    protected BirthDate() {
        this.date = null;
    }

    public BirthDate(LocalDate date) {
        validateNotNull(date);
        validateNotInFuture(date);
        validateMinimumAge(date);
        this.date = date;
    }

    private void validateNotNull(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new DomainException("A data de nascimento não pode ser nula.");
        }
    }

    private void validateNotInFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new DomainException("A data de nascimento não pode estar no futuro.");
        }
    }

    private void validateMinimumAge(LocalDate date) {
        // Regra de negócio: Exemplo de maioridade para o cadastro
        int age = Period.between(date, LocalDate.now()).getYears();
        if (age < 18) {
            throw new DomainException("O titular deve ter pelo menos 18 anos.");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAge() {
        return Period.between(this.date, LocalDate.now()).getYears();
    }
}