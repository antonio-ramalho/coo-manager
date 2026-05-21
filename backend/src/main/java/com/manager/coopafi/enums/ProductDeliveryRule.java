package com.manager.coopafi.enums;

import com.manager.coopafi.domain.entities.FarmerContract;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum ProductDeliveryRule {
    VALUE_ONLY("Somente por valor.") {
        @Override
        public void validateQuotaItem(Quantity maxQuantity) {
            if (maxQuantity != null && maxQuantity.getAmount() != null) {
                throw new DomainException("Para regras de entrega exclusivas por valor, a quantidade do " +
                        "produto não deve ser informada.");
            }
        }

        @Override
        public void validateStockPerQuota(FarmerContract farmerContract) {}
    },
    QUANTITY_LOCKED("Por quantidade de produto.") {
        @Override
        public void validateQuotaItem(Quantity maxQuantity) {
            if (maxQuantity == null || maxQuantity.getAmount() == null) {
                throw new DomainException("Para regras de entrega por produto, é obrigatório informar a " +
                        "quantidade máxima da cota.");
            }
        }

        @Override
        public void validateStockPerQuota(FarmerContract farmerContract) {
            Price totalAllocatedInProducts = farmerContract.getFarmerItemQuotas().stream()
                    .map(quota -> {
                        Price productPrice = quota.getContractedProduct().getFixedPrice();
                        BigDecimal quantity = quota.getMaxQuantity().getAmount();
                        return productPrice.multiply(quantity);
                    })
                    .reduce(new Price(BigDecimal.ZERO), Price::add);

            int comparison = totalAllocatedInProducts.getValue().compareTo(farmerContract.getSpecificCota().getValue());

            if (comparison > 0) {
                throw new DomainException("O valor dos produtos alocados para o(a) " + farmerContract.getFarmer()
                        .getPerson().getName() + " supera sua cota específica.");
            } else if (comparison < 0) {
                throw new DomainException("O valor dos produtos alocados para o(a) " + farmerContract.getFarmer()
                        .getPerson().getName() + " é inferior à sua cota " +
                        "específica. Aloque mais produtos ou reduza a cota.");
            }
        }
    };

    private final String description;

    public abstract void validateQuotaItem(Quantity maxQuantity);
    public abstract void validateStockPerQuota(FarmerContract contract);

    public static ProductDeliveryRule validateString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("É preciso cadastrar uma regra de entrega.");
        }

        for (ProductDeliveryRule rule : ProductDeliveryRule.values()) {
            if (rule.name().equalsIgnoreCase(value.trim())) {
                return rule;
            }
        }

        throw new DomainException("Regra de entrega invalida.");
    }
}
