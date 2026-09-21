package com.manager.coopafi.personModule.services;

import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.personModule.entities.Caf;
import com.manager.coopafi.personModule.entities.Farmer;
import com.manager.coopafi.personModule.entities.NaturalPerson;
import com.manager.coopafi.personModule.entities.OrganicCertificate;
import com.manager.coopafi.personModule.dto.farmer.FarmerDto;
import com.manager.coopafi.personModule.dto.farmer.FarmerInsertDto;
import com.manager.coopafi.personModule.dto.farmer.FarmerMinDto;
import com.manager.coopafi.personModule.dto.farmer.FarmerUpdateDto;
import com.manager.coopafi.personModule.enums.DocumentStatus;
import com.manager.coopafi.personModule.enums.Gender;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.personModule.enums.Status;
import com.manager.coopafi.personModule.repositories.CafRepository;
import com.manager.coopafi.personModule.repositories.FarmerRepository;
import com.manager.coopafi.personModule.repositories.NaturalPersonRepository;
import com.manager.coopafi.personModule.repositories.OrganicCertificateRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private NaturalPersonRepository personRepository;

    @Autowired
    private CafRepository cafRepository;

    @Autowired
    private OrganicCertificateRepository certificateRepository;

    @Transactional(readOnly = true)
    public List<FarmerMinDto> findAllByStatus() {
        List<Farmer> list = farmerRepository.findByStatus(Status.ACTIVE);
        return list.stream().map(FarmerMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public FarmerDto findByStatusAndId(Long id) {
        Farmer obj = farmerRepository.findByStatusAndId(Status.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Agricultor não encontrado"));

        return new FarmerDto(obj);
    }

    @Transactional
    public FarmerDto insert(FarmerInsertDto dto) {

        Address address = new Address(new Cep(dto.cepNumber()), dto.street(), dto.neighborhood(),
                dto.city(), dto.addressNumber());

        Gender gender = Gender.validateString(dto.gender());

        NaturalPerson nt = new NaturalPerson(address, new Email(dto.addressEmail()), new Phone(dto.phoneNumber()),
                dto.name(), new Cpf(dto.cpfNumber()), new BirthDate(dto.birthDate()), gender);

        if (personRepository.existsByCpf(nt.getCpf())) {
            throw new DomainException("O CPF informado já está cadastrado.");
        }

        nt = personRepository.save(nt);
        Farmer farmer = new Farmer(nt);

        linkDocuments(farmer, dto.cafId(), dto.certificateId());

        farmerRepository.save(farmer);
        return new FarmerDto(farmer);
    }

    @Transactional
    public FarmerDto update(Long id, FarmerUpdateDto dto) {
        Farmer farmer  = farmerRepository.findByStatusAndId(Status.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Agricultor não encontrado."));

        updateData(farmer, dto);
        linkDocuments(farmer, dto.cafId(), dto.certificateId());

        farmer = farmerRepository.save(farmer);
        return new FarmerDto(farmer);
    }

    @Transactional
    public void delete(Long id) {
        Farmer farmer = farmerRepository.findByStatusAndId(Status.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Agricultor não encontrado."));
        farmer.deactivate();
        farmerRepository.save(farmer);
    }

    private void updateData(Farmer farmer, FarmerUpdateDto dto) {
        NaturalPerson person = farmer.getPerson();

        if (dto.phoneNumber() != null) {
            person.updatePhone(new Phone(dto.phoneNumber()));
        }

        if (dto.addressEmail() != null) {
            person.updateEmail(new Email(dto.addressEmail()));
        }

        boolean idAddressUpdate = dto.cepNumber() != null ||
                dto.street() != null || dto.neighborhood() != null ||
                dto.city() != null || dto.addressNumber() != null;

        if (idAddressUpdate) {
            Address address = person.getAddress();
            Cep newCep = dto.cepNumber() != null ? new Cep(dto.cepNumber()) : address.getCep();
            String newStreet = dto.street() != null ? dto.street() : address.getStreet();
            String newCity = dto.city() != null ? dto.city() : address.getCity();
            String newNeighborhood = dto.neighborhood() != null ? dto.neighborhood() : address.getNeighborhood();
            String newAddressNumber = dto.addressNumber() != null ? dto.addressNumber() : address.getAddressNumber();

            Address newAddress = new Address(newCep, newStreet, newCity, newNeighborhood, newAddressNumber);
            person.updateAddress(newAddress);
        }
    }

    private void linkDocuments(Farmer farmer, Long cafId, Long  certificateId) {
        if (cafId != null) {
            Caf caf = cafRepository.findByDocumentStatusAndId(DocumentStatus.ACTIVE, cafId)
                    .orElseThrow(() -> new DomainException("Caf não encontrada."));
            farmer.linkCaf(caf);
        }

        if (certificateId != null) {
            OrganicCertificate certificate = certificateRepository.findByDocumentStatusAndId(DocumentStatus.ACTIVE, certificateId)
                    .orElseThrow(() -> new DomainException("Certificado orgânico não encontrado."));
            farmer.linkCertificate(certificate);
        }
    }
}
