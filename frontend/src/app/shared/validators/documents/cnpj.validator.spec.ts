import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { cnpjValidator } from './cnpj.validator';

describe('cnpjValidator', () => {
  it('deve aceitar campo vazio ou nulo', () => {
    expect(cnpjValidator(new FormControl(null))).toBeNull();
    expect(cnpjValidator(new FormControl(''))).toBeNull();
  });

  it('deve validar um CNPJ matematicamente correto', () => {
    expect(cnpjValidator(new FormControl('11222333000181'))).toBeNull();
    expect(cnpjValidator(new FormControl('11.222.333/0001-81'))).toBeNull();
  });

  it('deve rejeitar CNPJ com dígitos iguais', () => {
    expect(cnpjValidator(new FormControl('00.000.000/0000-00'))).toEqual({ invalidCnpj: true });
  });

  it('deve rejeitar CNPJ com dígitos verificadores incorretos', () => {
    expect(cnpjValidator(new FormControl('11.222.333/0001-82'))).toEqual({ invalidCnpj: true });
  });
});
