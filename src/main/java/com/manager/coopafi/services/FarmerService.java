package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.Caf;
import com.manager.coopafi.domain.entities.Farmer;
import com.manager.coopafi.domain.entities.NaturalPerson;
import com.manager.coopafi.domain.entities.OrganicCertificate;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.dto.farmer.FarmerDto;
import com.manager.coopafi.dto.farmer.FarmerInsertDto;
import com.manager.coopafi.dto.farmer.FarmerMinDto;
import com.manager.coopafi.dto.farmer.FarmerUpdateDto;
import com.manager.coopafi.enums.DocumentStatus;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.CafRepository;
import com.manager.coopafi.repositories.FarmerRepository;
import com.manager.coopafi.repositories.NaturalPersonRepository;
import com.manager.coopafi.repositories.OrganicCertificateRepository;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private CafRepository cafRepository;

    @Autowired
    private OrganicCertificateRepository certificateRepository;

    @Transactional(readOnly = true)
    public List<FarmerMinDto> findByStatus() {
        List<Farmer> list = farmerRepository.findByStatus(UserStatus.ACTIVE);
        return list.stream().map(FarmerMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public FarmerDto findByStatusAndId(Long id) {
        Optional<Farmer> obj = farmerRepository.findByStatusAndId(UserStatus.ACTIVE, id);
        if (obj.isEmpty()) {
            throw new DomainException("Agricultor não encontrado.");
        }
        return new FarmerDto(obj.get());
    }

    @Transactional
    public FarmerDto insert(FarmerInsertDto dto) {

        Address address = new Address(new Cep(dto.cepNumber()), dto.street(), dto.neighborhood(),
                dto.city(), dto.addressNumber());

        NaturalPerson nt = new NaturalPerson(address, new Email(dto.addressEmail()), new Phone(dto.phoneNumber()),
                dto.name(), new Cpf(dto.cpfNumber()), new BirthDate(dto.birthDate()));

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
        Farmer farmer  = farmerRepository.findByStatusAndId(UserStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Agricultor não encontrado."));

        updateData(farmer, dto);
        linkDocuments(farmer, dto.cafId(), dto.certificateId());

        farmer = farmerRepository.save(farmer);
        return new FarmerDto(farmer);
    }

    @Transactional
    public void delete(Long id) {
        Farmer farmer = farmerRepository.findByStatusAndId(UserStatus.ACTIVE, id)
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
