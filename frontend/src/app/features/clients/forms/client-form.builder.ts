import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonType } from '../../../shared/enums/person-type';

/**
 * Constrói a estrutura reativa do formulário de clientes (PF e PJ).
 */
export function buildClientForm(fb: FormBuilder): FormGroup {
  return fb.group({
    basicInfo: fb.group({
      legalName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
      personType: [PersonType.NATURAL, Validators.required],
    }),

    address: fb.group({
      zipCode: ['', Validators.required],
      street: ['', Validators.required],
      number: [''],
      city: ['', Validators.required],
      state: ['', Validators.required],
      neighborhood: ['', Validators.required],
    }),

    specificData: fb.group({
      cpf: [''],
      birthDate: [''],
      gender: [''],
      cnpj: [''],
      tradeName: [''],
      foundationDate: [''],
    }),
  });
}
