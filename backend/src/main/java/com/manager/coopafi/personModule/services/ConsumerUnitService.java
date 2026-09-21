package com.manager.coopafi.personModule.services;

import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.personModule.entities.Agent;
import com.manager.coopafi.personModule.entities.ConsumerUnit;
import com.manager.coopafi.personModule.dto.agent.AgentInsertDto;
import com.manager.coopafi.personModule.dto.consumerunit.ConsumerUnitDto;
import com.manager.coopafi.personModule.dto.consumerunit.ConsumerUnitInsertDto;
import com.manager.coopafi.personModule.dto.consumerunit.ConsumerUnitMinDto;
import com.manager.coopafi.personModule.dto.consumerunit.ConsumerUnitUpdateDto;
import com.manager.coopafi.personModule.enums.Gender;
import com.manager.coopafi.personModule.enums.Status;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.personModule.entities.JuridicPerson;
import com.manager.coopafi.personModule.entities.NaturalPerson;
import com.manager.coopafi.personModule.repositories.ConsumerUnitRepository;
import com.manager.coopafi.personModule.repositories.JuridicPersonRepository;
import com.manager.coopafi.personModule.repositories.NaturalPersonRepository;
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
    public List<ConsumerUnitMinDto> findAllByStatus() {
        List<ConsumerUnit> list = consumerUnitRepository.findByStatus(Status.ACTIVE);
        return list.stream().map(ConsumerUnitMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public ConsumerUnitDto findByStatusAndId(Long id) {
        ConsumerUnit entity = repository.findByStatusAndId(Status.ACTIVE, id)
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
                juridicAddress, new Email(dto.addressEmail()), new Phone(dto.phoneNumber()), dto.birthDate(),
                new Cnpj(dto.cnpj()), dto.legalName(), dto.tradeName()
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
                deliveryAddress, juridicPerson, dto.isSubsidiaryCNPJ());

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

        if (dto.legalName() != null) entity.getJuridicPerson().updateName(dto.legalName());
        if (dto.tradeName() != null) entity.getJuridicPerson().updateTradeName(dto.tradeName());
    }

    private Agent insertAgent(AgentInsertDto dtoAgent, Address juridicAddress) {
        Gender gender = Gender.validateString(dtoAgent.gender());

        NaturalPerson agentPerson = new NaturalPerson(
                juridicAddress, new Email(dtoAgent.agentEmail()), new Phone(dtoAgent.agentPhone()),
                dtoAgent.agentName(), new Cpf(dtoAgent.agentCpf()), new BirthDate(dtoAgent.agentBirthDate()), gender
        );

        if (naturalPersonRepository.existsByCpf(agentPerson.getCpf())) {
            throw new DomainException("O CPF informado já está cadastrado.");
        }

        agentPerson = naturalPersonRepository.save(agentPerson);

        return new Agent(dtoAgent.agentCargo(), agentPerson);
    }
}