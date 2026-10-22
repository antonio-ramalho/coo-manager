import { describe, expect, it } from 'vitest';
import { FormControl } from '@angular/forms';
import { CustomValidators } from './custom-validators';

describe('CustomValidators', () => {
  describe('cpf', () => {
    it('should return null for valid CPF', () => {
      // CPF matematicamente válido de teste (algoritmo padrão)
      const control = new FormControl('52998224725');
      expect(CustomValidators.cpf(control)).toBeNull();
    });

    it('should return error for invalid CPF', () => {
      const control = new FormControl('11111111111');
      expect(CustomValidators.cpf(control)).toEqual({ invalidCpf: true });
    });

    it('should return null for empty value', () => {
      const control = new FormControl('');
      expect(CustomValidators.cpf(control)).toBeNull();
    });
  });

  describe('cnpj', () => {
    it('should return null for valid CNPJ', () => {
      // CNPJ matematicamente válido de teste
      const control = new FormControl('11444777000161');
      expect(CustomValidators.cnpj(control)).toBeNull();
    });

    it('should return error for invalid CNPJ', () => {
      const control = new FormControl('11111111111111');
      expect(CustomValidators.cnpj(control)).toEqual({ invalidCnpj: true });
    });
  });

  describe('birthDate', () => {
    it('should return null for adult (18+)', () => {
      const control = new FormControl('2000-01-01');
      expect(CustomValidators.birthDate(control)).toBeNull();
    });

    it('should return error for minor (< 18)', () => {
      const today = new Date();
      const minorYear = today.getFullYear() - 10;
      const control = new FormControl(`${minorYear}-01-01`);
      expect(CustomValidators.birthDate(control)).toEqual({ minor: true });
    });
  });
});
