import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { cpfValidator } from './cpf.validator';

describe('cpfValidator', () => {
  it('deve aceitar campo vazio ou nulo (deixando para Validators.required)', () => {
    expect(cpfValidator(new FormControl(null))).toBeNull();
    expect(cpfValidator(new FormControl(''))).toBeNull();
  });

  it('deve validar um CPF matematicamente correto', () => {
    expect(cpfValidator(new FormControl('52998224725'))).toBeNull();
    expect(cpfValidator(new FormControl('529.982.247-25'))).toBeNull();
  });

  it('deve rejeitar CPF com tamanho incorreto', () => {
    expect(cpfValidator(new FormControl('1234567890'))).toEqual({ invalidCpf: true });
    expect(cpfValidator(new FormControl('123456789012'))).toEqual({ invalidCpf: true });
  });

  it('deve rejeitar CPF com todos os dígitos iguais', () => {
    expect(cpfValidator(new FormControl('111.111.111-11'))).toEqual({ invalidCpf: true });
    expect(cpfValidator(new FormControl('00000000000'))).toEqual({ invalidCpf: true });
  });

  it('deve rejeitar CPF com dígitos verificadores inválidos', () => {
    expect(cpfValidator(new FormControl('52998224726'))).toEqual({ invalidCpf: true });
  });
});
