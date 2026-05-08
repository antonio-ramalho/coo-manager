package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.*;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.dto.agent.AgentInsertDto;
import com.manager.coopafi.dto.consumerunit.ConsumerUnitDto;
import com.manager.coopafi.dto.consumerunit.ConsumerUnitInsertDto;
import com.manager.coopafi.dto.consumerunit.ConsumerUnitMinDto;
import com.manager.coopafi.dto.consumerunit.ConsumerUnitUpdateDto;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.ConsumerUnitRepository;
import com.manager.coopafi.repositories.JuridicPersonRepository;
import com.manager.coopafi.repositories.NaturalPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsumerUnitService {

    @Autowired
    private ConsumerUnitRepository repository;

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    @Autowired
    private NaturalPersonRepository naturalPersonRepository;

    @Autowired
    private ConsumerUnitRepository consumerUnitRepository;

    @Transactional(readOnly = true)
    public List<ConsumerUnitMinDto> findByStatus() {
        List<ConsumerUnit> list = consumerUnitRepository.findByStatus(UserStatus.ACTIVE);
        return list.stream().map(ConsumerUnitMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ConsumerUnitDto findByStatusAndId(Long id) {
        ConsumerUnit entity = repository.findByStatusAndId(UserStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Unidade Consumidora não encontrada."));
        return new ConsumerUnitDto(entity);
    }

    @Transactional
    public ConsumerUnitDto insert(ConsumerUnitInsertDto dto) {

        if (dto.agents() == null || dto.agents().isEmpty()) {
            throw new DomainException("A unidade deve ter pelo menos um representante.");
        }

        Address juridicAddress = new Address(
                new Cep(dto.cepNumber()), dto.street(), dto.neighborhood(), dto.city(), dto.addressNumber()
        );

        List <Agent> agents = dto.agents().stream()
                .map(agentInsertDto -> insertAgent(agentInsertDto, juridicAddress))
                .toList();

        JuridicPerson juridicPerson = new JuridicPerson(
                juridicAddress, new Email(dto.addressEmail()), new Phone(dto.phoneNumber()),
                new BirthDate(dto.birthDate()), new Cnpj(dto.cnpj()), dto.legalName(), dto.tradeName()
        );

        if (juridicPersonRepository.existsByCnpj(juridicPerson.getCnpj())) {
            throw new DomainException("O CNPJ informado já está cadastrado no sistema.");
        }

        juridicPerson = juridicPersonRepository.save(juridicPerson);

        Address deliveryAddress = new Address(
                new Cep(dto.deliveryCep()), dto.deliveryStreet(), dto.deliveryNeighborhood(),
                dto.deliveryCity(), dto.deliveryNumber()
        );

        ConsumerUnit unit = new ConsumerUnit(agents,
                deliveryAddress, juridicPerson);

        unit = repository.save(unit);

        return new ConsumerUnitDto(unit);
    }

    @Transactional
    public ConsumerUnitDto update(Long id, ConsumerUnitUpdateDto dto) {
        ConsumerUnit entity = repository.findById(id)
                .orElseThrow(() -> new DomainException("ID não encontrado: " + id));

        updateData(entity, dto);
        entity = repository.save(entity);
        return new ConsumerUnitDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        ConsumerUnit entity = repository.findById(id)
                .orElseThrow(() -> new DomainException("ID não encontrado."));
        entity.deactivate();
        repository.save(entity);
    }

    private void updateData(ConsumerUnit entity, ConsumerUnitUpdateDto dto) {

        if (dto.deliveryCep() != null) {
            Address current = entity.getDeliveryAddress();
            Address updated = new Address(
                    new Cep(dto.deliveryCep()),
                    dto.deliveryStreet() != null ? dto.deliveryStreet() : current.getStreet(),
                    dto.deliveryNeighborhood() != null ? dto.deliveryNeighborhood() : current.getNeighborhood(),
                    dto.deliveryCity() != null ? dto.deliveryCity() : current.getCity(),
                    dto.deliveryNumber() != null ? dto.deliveryNumber() : current.getAddressNumber()
            );
            entity.updateDeliveryAddress(updated);
        }

        if (dto.legalName() != null) entity.getJuridicPerson().updateLegalName(dto.legalName());
        if (dto.tradeName() != null) entity.getJuridicPerson().updateTradeName(dto.tradeName());
    }

    private Agent insertAgent(AgentInsertDto dtoAgent, Address juridicAddress) {
        NaturalPerson agentPerson = new NaturalPerson(
                juridicAddress, new Email(dtoAgent.agentEmail()), new Phone(dtoAgent.agentPhone()),
                dtoAgent.agentName(), new Cpf(dtoAgent.agentCpf()), new BirthDate(dtoAgent.agentBirthDate())
        );

        if (naturalPersonRepository.existsByCpf(agentPerson.getCpf())) {
            throw new DomainException("O CPF informado já está cadastrado.");
        }

        agentPerson = naturalPersonRepository.save(agentPerson);

        return new Agent(dtoAgent.agentCargo(), agentPerson);
    }
}