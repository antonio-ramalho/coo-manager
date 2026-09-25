package com.manager.coopafi.personModule.services;

import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.personModule.entities.Institution;
import com.manager.coopafi.personModule.entities.JuridicPerson;
import com.manager.coopafi.personModule.dto.institution.InstitutionDto;
import com.manager.coopafi.personModule.dto.institution.InstitutionInsertDto;
import com.manager.coopafi.personModule.dto.institution.InstitutionMinDto;
import com.manager.coopafi.personModule.dto.institution.InstitutionUpdateDto;
import com.manager.coopafi.personModule.enums.InstitutionSphere;
import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.personModule.repositories.InstitutionRepository;
import com.manager.coopafi.personModule.repositories.JuridicPersonRepository;
import com.manager.coopafi.personModule.enums.Status;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private JuridicPersonRepository juridicPersonRepository;

    @Transactional(readOnly = true)
    public List<InstitutionMinDto> findAllByStatus() {
        List<Institution> list = institutionRepository.findByStatus(Status.ACTIVE);
        return list.stream().map(InstitutionMinDto::new).toList();
    }

    @Transactional(readOnly = true)
    public InstitutionDto findByStatusAndId(Long id) {
        Institution obj = institutionRepository.findByStatusAndId(Status.ACTIVE, id)
            .orElseThrow(() -> new DomainException("Instituição não encontrada."));
        return new InstitutionDto(obj);
    }

    @Transactional
    public InstitutionDto insert(InstitutionInsertDto dto) {
        Cnpj cnpj = new Cnpj(dto.cnpjNumber());
        Cep cep = new Cep(dto.address().zipCode());
        Phone phone = new Phone(dto.phoneNumber());
        Email email = new Email(dto.addressEmail());
        Address address = new Address(cep, dto.address().street(), dto.address().neighborhood(), dto.address().city(),
                dto.address().number(), dto.address().state());

        JuridicPerson juridicPerson = new JuridicPerson(address, email, phone, dto.birthDate(), cnpj, dto.legalName(), dto.tradeName());

        if (juridicPersonRepository.existsByCnpj(juridicPerson.getCnpj())) {
            throw new DomainException("O CNPJ informado já está cadastrado no sistema.");
        }

        juridicPerson = juridicPersonRepository.save(juridicPerson);

        InstitutionSphere sphere = InstitutionSphere.valueOf(dto.institutionSphere());
        Institution institution = new Institution(sphere, juridicPerson);

        institutionRepository.save(institution);
        return new InstitutionDto(institution);
    }

    @Transactional
    public InstitutionDto update(Long id, InstitutionUpdateDto dto) {
        Institution institution = institutionRepository.findByStatusAndId(Status.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Instituição não encontrada."));

        updateData(institution, dto);

        institution = institutionRepository.save(institution);
        return new InstitutionDto(institution);
    }

    @Transactional
    public void delete(Long id) {
        Institution institution = institutionRepository.findByStatusAndId(Status.ACTIVE, id)
                .orElseThrow(() -> new DomainException("Instituição não encontrada."));
        institution.deactivate();
        institutionRepository.save(institution);
    }

    private void updateData(Institution institution, InstitutionUpdateDto dto) {
        JuridicPerson juridicPerson = institution.getJuridicPerson();

        if (dto.tradeName() != null) {
            juridicPerson.updateTradeName(dto.tradeName());
        }

        if (dto.legalName() != null) {
            juridicPerson.updateName(dto.legalName());
        }

        if (dto.phoneNumber() != null) {
            juridicPerson.updatePhone(new Phone(dto.phoneNumber()));
        }

        if (dto.addressEmail() != null) {
            juridicPerson.updateEmail(new Email(dto.addressEmail()));
        }

        boolean isAddressUpdate = dto.address().zipCode() != null ||
                dto.address().street() != null || dto.address().neighborhood() != null ||
                dto.address().city() != null || dto.address().number() != null;

        if (isAddressUpdate) {
            Address address = juridicPerson.getAddress();
            Cep newCep = dto.address().zipCode() != null ? new Cep(dto.address().zipCode()) : address.getCep();
            String newStreet = dto.address().street() != null ? dto.address().street() : address.getStreet();
            String newCity = dto.address().city() != null ? dto.address().city() : address.getCity();
            String newNeighborhood = dto.address().neighborhood() != null ? dto.address().neighborhood() : address.getNeighborhood();
            String newAddressNumber = dto.address().number() != null ? dto.address().number() : address.getAddressNumber();

            Address newAddress = new Address(newCep, newStreet, newCity, newNeighborhood, newAddressNumber, dto.address().state());
            juridicPerson.updateAddress(newAddress);
        }
    }
}
