package com.manager.coopafi.personModule.resources;

import com.manager.coopafi.personModule.dto.agent.AgentDto;
import com.manager.coopafi.personModule.dto.agent.AgentUpdateDto;
import com.manager.coopafi.personModule.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/consumerunits/{unitId}/agents")
public class AgentResources {

    @Autowired
    private AgentService service;

    @PatchMapping(value = "/{agentId}")
    public ResponseEntity<AgentDto> updateAgent(
            @PathVariable Long unitId,
            @PathVariable Long agentId,
            @RequestBody AgentUpdateDto agentUpdateDto) {
        AgentDto updatedAgent = service.updateAgent(unitId, agentId, agentUpdateDto);
        return ResponseEntity.ok().body(updatedAgent);
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity <Void> delete(
            @PathVariable Long unitId,
            @PathVariable Long agentId
    ) {
        service.deleteAgent(unitId, agentId);
        return ResponseEntity.noContent().build();
    }
}
