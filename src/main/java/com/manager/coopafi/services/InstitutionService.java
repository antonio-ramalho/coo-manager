package com.manager.coopafi.services;

import com.manager.coopafi.domain.entities.Institution;
import com.manager.coopafi.domain.entities.JuridicPerson;
import com.manager.coopafi.domain.valueObjects.*;
import com.manager.coopafi.dto.institution.InstitutionDto;
import com.manager.coopafi.dto.institution.InstitutionInsertDto;
import com.manager.coopafi.dto.institution.InstitutionMinDto;
import com.manager.coopafi.dto.institution.InstitutionUpdateDto;
import com.manager.coopafi.enums.InstitutionSphere;
import com.manager.coopafi.enums.UserStatus;
import com.manager.coopafi.exceptions.DomainException;
import com.manager.coopafi.repositories.InstitutionRepository;
import com.manager.coopafi.repositories.JuridicPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    public List<InstitutionMinDto> findByStatus() {
        List<Institution> list = institutionRepository.findByStatus(UserStatus.ACTIVE);
        return list.stream().map(InstitutionMinDto::new).toList();
    }

    public InstitutionDto findByStatusAndId(Long id) {
        Optional<Institution> obj = institutionRepository.findByStatusAndId(UserStatus.ACTIVE, id);
        if (obj.isEmpty()) {
            throw new DomainException("Instituição não encontrada.");
        }
        return new InstitutionDto(obj.get());
    }

    @Transactional
    public InstitutionDto insert(InstitutionInsertDto dto) {
        Cnpj cnpj = new Cnpj(dto.cnpjNumber());
        Cep cep = new Cep(dto.cepNumber());
        Phone phone = new Phone(dto.phoneNumber());
        Email email = new Email(dto.addressEmail());
        Address address = new Address(cep, dto.street(), dto.neighborhood(), dto.city(), dto.addressNumber());
        BirthDate birthDate = new BirthDate(dto.birthDate());

        JuridicPerson juridicPerson = new JuridicPerson(address, email, phone, birthDate, cnpj, dto.legalName(), dto.tradeName());
        juridicPerson = juridicPersonRepository.save(juridicPerson);

        InstitutionSphere sphere = InstitutionSphere.valueOf(dto.institutionSphere());
        Institution institution = new Institution(sphere, juridicPerson);

        institutionRepository.save(institution);
        return new InstitutionDto(institution);
    }

    @Transactional
    public InstitutionDto update(Long id, InstitutionUpdateDto dto) {
        Institution institution = institutionRepository.findByStatusAndId(UserStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Instituição não encontrada."));

        updateData(institution, dto);

        institution = institutionRepository.save(institution);
        return new InstitutionDto(institution);
    }

    private void updateData(Institution institution, InstitutionUpdateDto dto) {
        JuridicPerson juridicPerson = institution.getJuridicPerson();

        if (dto.tradeName() != null) {
            juridicPerson.updateTradeName(dto.tradeName());
        }

        if (dto.legalName() != null) {
            juridicPerson.updateLegalName(dto.legalName());
        }

        if (dto.phoneNumber() != null) {
            juridicPerson.updatePhone(new Phone(dto.phoneNumber()));
        }

        if (dto.addressEmail() != null) {
            juridicPerson.updateEmail(new Email(dto.addressEmail()));
        }

        boolean isAddressUpdate = dto.cepNumber() != null ||
                dto.street() != null || dto.neighborhood() != null ||
                dto.city() != null || dto.addressNumber() != null;

        if (isAddressUpdate) {
            Address address = juridicPerson.getAddress();
            Cep newCep = dto.cepNumber() != null ? new Cep(dto.cepNumber()) : address.getCep();
            String newStreet = dto.street() != null ? dto.street() : address.getStreet();
            String newCity = dto.city() != null ? dto.city() : address.getCity();
            String newNeighborhood = dto.neighborhood() != null ? dto.neighborhood() : address.getNeighborhood();
            String newAddressNumber = dto.addressNumber() != null ? dto.addressNumber() : address.getAddressNumber();

            Address newAddress = new Address(newCep, newStreet, newCity, newNeighborhood, newAddressNumber);
            juridicPerson.updateAddress(newAddress);
        }
    }

    @Transactional
    public void delete(Long id) {
        Institution institution = institutionRepository.findByStatusAndId(UserStatus.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Instituição não encontrada."));
        institution.deactivate();
        institutionRepository.save(institution);
    }
}

