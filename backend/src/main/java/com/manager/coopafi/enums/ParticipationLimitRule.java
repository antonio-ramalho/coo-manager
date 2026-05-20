package com.manager.coopafi.enums;

import com.manager.coopafi.domain.entities.Contract;
import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.FarmerContract;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum ParticipationLimitRule {
    PER_CPF("Por Cpf") {
        @Override
        public void validate(Contract contract, Farmer farmer, Price quota) {
            if (quota == null || quota.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new DomainException("A cota do " + farmer.getPerson().getName() + " não pode estar zerada.");
            }

            if (quota.getValue().compareTo(contract.getGlobalLimit().getValue()) > 0) {
                throw new DomainException("A cota do(a) " + farmer.getPerson().getName() + " ultrapassa o limite.");
            }
        }
    },
    PER_CAF("Por Caf") {
        @Override
        public void validate(Contract contract, Farmer farmer, Price quota) {
            if (quota == null || quota.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new DomainException("A cota do " + farmer.getPerson().getName() + " não pode estar zerada.");
            }

            Price accumulator = contract.getFarmerContracts().stream()
                    .filter(fc -> fc.getFarmer().getCaf().equals(farmer.getCaf()))
                    .map(FarmerContract::getSpecificCota)
                    .reduce(new Price(BigDecimal.ZERO), Price::add);

            Price projectedTotal = accumulator.add(quota);

            if (projectedTotal.compareTo(contract.getGlobalLimit()) > 0) {
                throw new DomainException("O limite de " + contract.getGlobalLimit().getValue() + " para a Caf estourou.");
            }
        }
    },
    UNLIMITED("Sem limite") {
        @Override
        public void validate(Contract contract, Farmer farmer, Price quota) {
        }
    };

    private final String description;

    public abstract void validate(Contract contract, Farmer farmer, Price quota);

    public static ParticipationLimitRule validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("É preciso cadastrar uma regra de pagamento.");
        }

        for (ParticipationLimitRule rule : ParticipationLimitRule.values()) {
            if (rule.name().equalsIgnoreCase(value.trim())) {
                return rule;
            }
        }

        throw new DomainException("Regra de pagamento invalida.");
    }
}


