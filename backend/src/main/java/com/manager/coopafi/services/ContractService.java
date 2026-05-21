package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.Price;
import com.manager.coopafi.domain.valueObjects.Quantity;
import com.manager.coopafi.dto.contract.ContractDto;
import com.manager.coopafi.dto.contract.ContractInsertDto;
import com.manager.coopafi.dto.contract.ContractMinDto;
import com.manager.coopafi.dto.contract.ContractUpdateDto;
import com.manager.coopafi.dto.contractConsumer.ContractConsumerInsertDto;
import com.manager.coopafi.dto.contractedProduct.ContractedProductInsertDto;
import com.manager.coopafi.dto.farmerContractDto.FarmerContractInsertDto;
import com.manager.coopafi.enums.ParticipationLimitRule;
import com.manager.coopafi.enums.ProductDeliveryRule;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private AgriculturalProductRepository agriculturalProductRepository;

    @Autowired
    private ConsumerUnitRepository consumerUnitRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    @Transactional(readOnly = true)
    public List<ContractMinDto> findAll() {
        List<Contract> list = contractRepository.findAll();
        return list.stream().map(ContractMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ContractDto findById(Long id) {
        Contract obj =  contractRepository.findById(id)
                .orElseThrow(() -> new DomainException("Contrato não encontrado."));
        return new ContractDto(obj);
    }

    @Transactional
    public ContractDto create(ContractInsertDto dto) {

        if (dto.farmerContracts() == null || dto.farmerContracts().isEmpty()) {
            throw new DomainException("É necessário pelo menos um agricultor.");
        }

        if (dto.contractConsumers() == null || dto.contractConsumers().isEmpty()) {
            throw new DomainException("É necessário pelo menos uma unidade consumidora.");
        }

        if (dto.products() == null || dto.products().isEmpty()) {
            throw new DomainException("É necessário pelo menos um produto cadastrado.");
        }

        Institution institution = institutionRepository.findById(dto.institutionId())
                .orElseThrow(() -> new DomainException("Instituição não encontrada."));

        ParticipationLimitRule limitRule = ParticipationLimitRule.validateString(dto.participationRule());
        ProductDeliveryRule deliveryRule = ProductDeliveryRule.validateString(dto.productDeliveryRule());
        Price totalContractValue = new Price(new BigDecimal(dto.totalContractValue()));
        Price globalLimit = new Price(new BigDecimal(dto.globalLimit()));

        Contract contract = new Contract(dto.finalContractDate(), totalContractValue,
                globalLimit, limitRule, deliveryRule, institution);

        instantiateProduct(contract, dto.products());
        instantiateConsumerUnits(contract, dto.contractConsumers());
        instantiateFarmerContracts(contract, dto.farmerContracts());
        contract.validateContractIntegrity();

        contractRepository.save(contract);
        return new ContractDto(contract);
    }

    private void instantiateProduct(Contract contract,List <ContractedProductInsertDto> dtoList) {
        for (ContractedProductInsertDto dto : dtoList) {
            AgriculturalProduct agriculturalProduct = agriculturalProductRepository.findById(dto.agriculturalProductId())
                    .orElseThrow(() -> new DomainException("Produto não encontrado no catalogo."));

            Quantity quantity = null;
            if (dto.quantity() != null) {
                quantity = new Quantity(BigDecimal.valueOf(dto.quantity()));
            }

            contract.addContractedProduct(agriculturalProduct, new Price(new BigDecimal(dto.fixedPrice())),
                    dto.productName(), quantity);
        }
    }

    private void instantiateConsumerUnits(Contract contract, List<ContractConsumerInsertDto> dtoList) {
        for (ContractConsumerInsertDto dto : dtoList) {
            ConsumerUnit unit = consumerUnitRepository.findByStatusAndId(UserStatus.ACTIVE, dto.consumerId())
                    .orElseThrow(() -> new DomainException("Unidade consumidora não encontrada."));

            contract.addConsumerUnit(unit);
        }
    }

    private void instantiateFarmerContracts(Contract contract, List<FarmerContractInsertDto> dtoList) {
        for (FarmerContractInsertDto dto : dtoList) {
            Farmer farmer = farmerRepository.findByStatusAndId(UserStatus.ACTIVE, dto.farmerId())
                    .orElseThrow(() -> new DomainException("Agricultor não encontrado."));

            FarmerContract participation = contract.addFarmerParticipation(farmer,
                    new Price(new BigDecimal(dto.specificCota())));

            if (dto.quotas() == null) {
                throw new DomainException("Não foi inserida as cotas para atender as regras.");
            }

            for (var quotaDto : dto.quotas()) {
                ContractedProduct contractedProduct = contract.getProducts().stream()
                        .filter(cp -> cp.getProduct().getId().equals(quotaDto.agriculturalProductId()))
                        .findFirst()
                        .orElseThrow(() -> new DomainException("O produto de ID " + quotaDto.agriculturalProductId() + " não faz parte deste edital."));

                Quantity maxQuantity = quotaDto.maxQuantity() != null ? new Quantity(new BigDecimal(quotaDto.maxQuantity())) : null;

                participation.addProductQuota(contractedProduct, maxQuantity);
            }
        }
    }

    @Transactional
    public ContractDto update(ContractUpdateDto dto) {
        return null;
    }

    @Transactional
    public void delete(Long id) {
        Contract obj = contractRepository.findById(id)
                .orElseThrow(() -> new DomainException("Contrato não encontrado."));

        obj.deactivate();
        contractRepository.save(obj);
    }
}
