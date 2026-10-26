import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { CustomValidators } from '../../../shared/validators/custom-validators';
import { Gender } from '../../../shared/enums/gender';
import { PersonType } from '../../../shared/enums/person-type';
import { Client, ClientOf } from '../models/client.model';

/** Campos do grupo `specificData` do formulário de cliente (união dos campos de PF e PJ). */
export type SpecificDataField = 'cpf' | 'birthDate' | 'gender' | 'cnpj' | 'tradeName' | 'foundationDate';

/** Parte dos detalhes de exibição que varia conforme a natureza jurídica. */
export interface PersonTypeDetails {
  documentNumber: string;
  dateValue: string;
  gender: Gender | null;
}

/**
 * Estratégia de regras por natureza jurídica (padrão Strategy indexado pelo enum).
 * Para incluir um novo tipo de pessoa basta adicionar uma entrada em `PERSON_TYPE_RULES`:
 * nenhum `if/else` precisa ser alterado nos componentes.
 */
export interface PersonTypeRule {
  documentLabel: string;
  dateLabel: string;
  /** Campo de documento, que fica bloqueado durante a edição. */
  documentField: Extract<SpecificDataField, 'cpf' | 'cnpj'>;
  /** Validadores ativos para o tipo; campos ausentes ficam sem validação. */
  validators: Partial<Record<SpecificDataField, ValidatorFn[]>>;
  toDetails(client: Client): PersonTypeDetails;
}

export const PERSON_TYPE_RULES: Record<PersonType, PersonTypeRule> = {
  [PersonType.NATURAL]: {
    documentLabel: 'Nº CPF',
    dateLabel: 'Data de nascimento',
    documentField: 'cpf',
    validators: {
      cpf: [Validators.required, CustomValidators.cpf],
      birthDate: [Validators.required, CustomValidators.birthDate],
      gender: [Validators.required],
    },
    toDetails: (client) => {
      const natural = client as ClientOf<PersonType.NATURAL>;
      return {
        documentNumber: natural.cpf,
        dateValue: natural.birthDate,
        gender: natural.gender,
      };
    },
  },
  [PersonType.JURIDIC]: {
    documentLabel: 'Nº CNPJ',
    dateLabel: 'Data de fundação',
    documentField: 'cnpj',
    validators: {
      cnpj: [Validators.required, CustomValidators.cnpj],
      tradeName: [Validators.required],
      foundationDate: [Validators.required],
    },
    toDetails: (client) => {
      const juridic = client as ClientOf<PersonType.JURIDIC>;
      return {
        documentNumber: juridic.cnpj,
        dateValue: juridic.foundationDate,
        gender: null,
      };
    },
  },
};

/** Obtém a regra de um tipo, já tipada de forma genérica para uso com a união `Client`. */
export function getPersonTypeRule(type: PersonType): PersonTypeRule {
  return PERSON_TYPE_RULES[type];
}

/**
 * Aplica no grupo `specificData` apenas os validadores do tipo informado:
 * limpa todos os controles e reaplica os definidos na estratégia.
 */
export function applyPersonTypeValidators(group: FormGroup, type: PersonType): void {
  const { validators } = getPersonTypeRule(type);

  Object.entries(group.controls).forEach(([field, control]) => {
    control.setValidators(validators[field as SpecificDataField] ?? null);
    control.updateValueAndValidity();
  });
}
