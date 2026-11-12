package com.manager.coomanager.personModule.services;

import com.manager.coomanager.personModule.entities.Agent;
import com.manager.coomanager.personModule.entities.ConsumerUnit;
import com.manager.coomanager.infrastructure.valueObjects.Email;
import com.manager.coomanager.infrastructure.valueObjects.Phone;
import com.manager.coomanager.personModule.dto.agent.AgentDto;
import com.manager.coomanager.personModule.dto.agent.AgentUpdateDto;
import com.manager.coomanager.infrastructure.exceptions.DomainException;
import com.manager.coomanager.personModule.enums.Status;
import com.manager.coomanager.personModule.repositories.ConsumerUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService {

    @Autowired
    private ConsumerUnitRepository consumerUnitRepository;

    @Transactional
    public AgentDto updateAgent(Long unitId, Long agentId, AgentUpdateDto dto) {
        ConsumerUnit unit = consumerUnitRepository.findByStatusAndId(Status.ACTIVE, unitId)
                .orElseThrow(() -> new DomainException("Unidade Consumidora não encontrada."));

        Agent agentToUpdate = unit.getAgents().stream()
                .filter(agent -> agent.getId().equals(agentId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Agente não encontrado."));

        if (dto.agentName() != null) {
            agentToUpdate.getNatPerson().updateName(dto.agentName());
        }

        if (dto.agentCargo() != null) {
            agentToUpdate.changeCargo(dto.agentCargo());
        }

        if (dto.agentEmail() !=  null) {
            agentToUpdate.getNatPerson().updateEmail(new Email(dto.agentEmail()));
        }

        if (dto.agentPhone() != null) {
            agentToUpdate.getNatPerson().updatePhone(new Phone(dto.agentPhone()));
        }

        consumerUnitRepository.save(unit);
        return new AgentDto(agentToUpdate);
    }

    @Transactional
    public void deleteAgent(Long unitId, Long agentId) {
        ConsumerUnit entity = consumerUnitRepository.findByStatusAndId(Status.ACTIVE, unitId)
                .orElseThrow(() -> new DomainException("Unidade Consumidora não encontrada."));

        Agent agent = entity.getAgents().stream()
                .filter(a -> a.getId().equals(agentId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Agente não encontrado."));

        entity.removeAgent(agent);

        consumerUnitRepository.save(entity);
    }
}
