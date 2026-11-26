package com.manager.coomanager.contractModule.resources;

import com.manager.coomanager.contractModule.dto.agent.AgentDto;
import com.manager.coomanager.contractModule.dto.agent.AgentUpdateDto;
import com.manager.coomanager.contractModule.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/consumerunits/{unitId}/agents")
public class AgentResources {

    @Autowired
    private AgentService service;

    @PatchMapping(value = "/{agentId}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<AgentDto> updateAgent(
            @PathVariable Long unitId,
            @PathVariable Long agentId,
            @RequestBody AgentUpdateDto agentUpdateDto) {
        AgentDto updatedAgent = service.updateAgent(unitId, agentId, agentUpdateDto);
        return ResponseEntity.ok().body(updatedAgent);
    }

    @DeleteMapping("/{agentId}")
    @PreAuthorize("hasAuthority('CONTRACT_WRITE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long unitId,
            @PathVariable Long agentId) {
        service.deleteAgent(unitId, agentId);
        return ResponseEntity.noContent().build();
    }
}
