import { FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { CustomValidators } from '../../../shared/validators';
import { Gender } from '../../../shared/enums/gender';
import { PersonType } from '../../../shared/enums/person-type';
import { Client, ClientOf } from '../models/client.model';

export type SpecificDataField = 'cpf' | 'birthDate' | 'gender' | 'cnpj' | 'tradeName' | 'foundationDate';

export interface PersonTypeDetails {
  documentNumber: string;
  dateValue: string;
  gender: Gender | null;
}

export interface PersonTypeRule {
  documentLabel: string;
  dateLabel: string;
  documentField: Extract<SpecificDataField, 'cpf' | 'cnpj'>;
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

export function getPersonTypeRule(type: PersonType): PersonTypeRule {
  return PERSON_TYPE_RULES[type];
}

export function applyPersonTypeValidators(group: FormGroup, type: PersonType): void {
  const { validators } = getPersonTypeRule(type);

  Object.entries(group.controls).forEach(([field, control]) => {
    control.setValidators(validators[field as SpecificDataField] ?? null);
    control.updateValueAndValidity();
  });
}
