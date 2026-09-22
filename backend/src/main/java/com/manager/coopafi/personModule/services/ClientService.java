package com.manager.coopafi.personModule.services;

import com.manager.coopafi.infrastructure.exceptions.DomainException;
import com.manager.coopafi.infrastructure.exceptions.ResourceNotFoundException;
import com.manager.coopafi.infrastructure.valueObjects.*;
import com.manager.coopafi.infrastructure.valueObjectsDto.AddressDto;
import com.manager.coopafi.personModule.dto.client.ClientMinDto;
import com.manager.coopafi.personModule.dto.client.JuridicClientDto;
import com.manager.coopafi.personModule.dto.client.NaturalClientDto;
import com.manager.coopafi.personModule.entities.JuridicPerson;
import com.manager.coopafi.personModule.entities.NaturalPerson;
import com.manager.coopafi.personModule.entities.Person;
import com.manager.coopafi.personModule.enums.Gender;
import com.manager.coopafi.personModule.enums.Status;
import com.manager.coopafi.personModule.interfaces.IClientDto;
import com.manager.coopafi.personModule.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    @Autowired
    private PersonRepository repository;

    @Transactional(readOnly = true)
    public Page<ClientMinDto> findAllByPage(Pageable pageable, String searchTerm) {
        String search = searchTerm.replaceAll("[.\\-/]", "");
        Page<Person> personPage =  repository.findAllByStatusAndSearchTerm(search, pageable);

        return personPage.map(ClientMinDto::new);
    }

    @Transactional(readOnly = true)
    public IClientDto findById(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        if (person instanceof NaturalPerson np) {
            return new NaturalClientDto(np);
        }
        else if (person instanceof JuridicPerson jp) {
            return new JuridicClientDto(jp);
        }
        throw new DomainException("Tipo de cliente desconhecido no sistema.");
    }

    @Transactional()
    public IClientDto insert(IClientDto clientDto) {

        if (clientDto instanceof NaturalClientDto naturalDto) {
            NaturalPerson np = instanceNaturalPerson(naturalDto);
            np = repository.save(np);
            return new NaturalClientDto(np);
        }
        else if (clientDto instanceof JuridicClientDto juridicDto) {
            JuridicPerson  jp = instanceJuridicPerson(juridicDto);
            jp = repository.save(jp);
            return new JuridicClientDto(jp);
        }
        throw new IllegalArgumentException("Tipo de cliente não suportado!");
    }

    @Transactional
    public IClientDto update(Long id, IClientDto dto) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        Person updatedPerson = repository.save(updateData(dto, person));

        if (updatedPerson instanceof NaturalPerson np) {
            return new NaturalClientDto(np);
        }
        else if (updatedPerson instanceof JuridicPerson jp) {
            return new JuridicClientDto(jp);
        }
        throw new DomainException("Tipo de cliente desconhecido no sistema.");
    }

    @Transactional
    public void delete(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado!"));

        person.deactivate();
        repository.save(person);
    }

    private Person updateData(IClientDto dto, Person person) {
        person.updateName(dto.legalName());
        person.updateEmail(new Email(dto.email()));
        person.updatePhone(new Phone(dto.phone()));

        if (dto.address() != null) {
            person.updateAddress(createAddressFromDto(dto.address()));
        }

        if (dto instanceof NaturalClientDto naturalDto && person instanceof NaturalPerson np) {
            np.updateGender(naturalDto.gender());
            np.updateBirthDate(new BirthDate(naturalDto.birthDate()));
        }
        else if (dto instanceof JuridicClientDto juridicDto && person instanceof JuridicPerson jp) {

            jp.updateTradeName(juridicDto.tradeName());
            jp.updateFoundationDate(juridicDto.foundationDate());
        }
        else {
            throw new DomainException("Operação inválida: Não é possível alterar a natureza jurídica (Física/Jurídica) do cliente.");
        }
        return person;
    }

    private NaturalPerson instanceNaturalPerson(NaturalClientDto naturalDto) {
        Address address = createAddressFromDto(naturalDto.address());
        Email email = new Email(naturalDto.email());
        Phone phone = new Phone(naturalDto.phone());
        Cpf cpf = new Cpf(naturalDto.cpf());
        BirthDate birthDate = new BirthDate(naturalDto.birthDate());
        Gender gender = naturalDto.gender();

        return new NaturalPerson(address, email, phone, naturalDto.legalName(),  cpf, birthDate, gender);
    }

    private JuridicPerson instanceJuridicPerson(JuridicClientDto juridicDto) {
        Address address = createAddressFromDto(juridicDto.address());
        Email email = new Email(juridicDto.email());
        Phone phone = new Phone(juridicDto.phone());
        Cnpj  cnpj = new Cnpj(juridicDto.cnpj());

        return new JuridicPerson(address, email, phone, juridicDto.foundationDate(),
                cnpj, juridicDto.legalName(), juridicDto.tradeName());
    }

    private Address createAddressFromDto(AddressDto dto) {
        if (dto == null) return null;
        return new Address(new Cep(dto.zipCode()), dto.street(),
                dto.neighborhood(), dto.city(), dto.number());
    }
}
