package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.repositories.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository repository;

    public List<Farmer> findAll() {
        return repository.findAll();
    }

    public Farmer findById(Long id) {
        Optional<Farmer> obj = repository.findById(id);
        return obj.get();
    }
}
