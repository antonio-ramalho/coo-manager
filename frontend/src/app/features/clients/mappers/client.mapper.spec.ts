import { describe, expect, it } from 'vitest';
import { PersonType } from '../../../shared/enums/person-type';
import { Status } from '../../../shared/enums/status';
import { Gender } from '../../../shared/enums/gender';
import { Client, ClientSummary } from '../models/client.model';
import { toClientDetails, toListCardItem } from './client.mapper';

describe('client.mapper', () => {
  it('should map ClientSummary to ListCardItem with formatted document', () => {
    const summary: ClientSummary = {
      id: 1,
      legalName: 'Cooperativa Agro',
      documentNumber: '12345678000199',
      status: Status.ACTIVE,
    };

    const item = toListCardItem(summary);

    expect(item.id).toBe(1);
    expect(item.title).toBe('Cooperativa Agro');
    expect(item.subtitle).toBe('12.345.678/0001-99');
    expect(item.isActive).toBe(true);
  });

  it('should map natural client to ClientDetails correctly', () => {
    const client: Client = {
      id: 10,
      legalName: 'João da Silva',
      email: 'joao@email.com',
      phone: '42999998888',
      status: Status.ACTIVE,
      personType: PersonType.NATURAL,
      cpf: '12345678901',
      birthDate: '1990-05-15',
      gender: Gender.MALE,
      address: {
        zipCode: '85000000',
        street: 'Rua Principal',
        number: '100',
        neighborhood: 'Centro',
        city: 'Guarapuava',
        state: 'PR',
      },
    };

    const details = toClientDetails(client);

    expect(details.id).toBe(10);
    expect(details.documentLabel).toBe('Nº CPF');
    expect(details.documentNumber).toBe('12345678901');
    expect(details.dateLabel).toBe('Data de nascimento');
    expect(details.dateValue).toBe('1990-05-15');
    expect(details.gender).toBe(Gender.MALE);
  });

  it('should map juridic client to ClientDetails correctly', () => {
    const client: Client = {
      id: 20,
      legalName: 'Empresa Alimentos Ltda',
      email: 'contato@alimentos.com',
      phone: '4233332222',
      status: Status.ACTIVE,
      personType: PersonType.JURIDIC,
      cnpj: '12345678000199',
      foundationDate: '2010-01-20',
      tradeName: 'Alimentos Sul',
      address: {
        zipCode: '85000000',
        street: 'Av Industrial',
        number: '500',
        neighborhood: 'Distrito',
        city: 'Guarapuava',
        state: 'PR',
      },
    };

    const details = toClientDetails(client);

    expect(details.id).toBe(20);
    expect(details.documentLabel).toBe('Nº CNPJ');
    expect(details.documentNumber).toBe('12345678000199');
    expect(details.dateLabel).toBe('Data de fundação');
    expect(details.dateValue).toBe('2010-01-20');
    expect(details.gender).toBeNull();
  });
});
