package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.dto.FarmerDto;
import com.manager.coopafi.dto.FarmerInsertDTO;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.NaturalPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private NaturalPersonRepository personRepository;

    public List<FarmerDto> findAll() {
        List<Farmer> list = farmerRepository.findAll();
        return list.stream().map(FarmerDto::new).toList();
    }

    public FarmerDto findById(Long id) {
        Optional<Farmer> obj = farmerRepository.findById(id);
        FarmerDto dto = new FarmerDto(obj.get());
        return dto;
    }

    @Transactional
    public FarmerDto insert(FarmerInsertDTO dto) {
        Cpf cpf = new Cpf(dto.cpfNumber());
        Cep cep = new Cep(dto.cepNumber());
        BirthDate birthDate = new BirthDate(dto.date());
        Phone  phone = new Phone(dto.phoneNumber());
        Email email = new Email(dto.addressEmail());
        Address address = new Address(cep, dto.street(), dto.neighborhood(), dto.city(), dto.addressNumber());

        NaturalPerson nt = new NaturalPerson(address, email, phone, dto.name(), cpf, birthDate);
        nt = personRepository.save(nt);
        Farmer farmer = new Farmer(nt);
        farmerRepository.save(farmer);

        return new FarmerDto(farmer);
    }
}
