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

  describe('ncm', () => {
    it('should return null for valid 8-digit NCM', () => {
      const control = new FormControl('31010000');
      expect(CustomValidators.ncm(control)).toBeNull();
    });

    it('should return null for valid masked NCM', () => {
      const control = new FormControl('3101.00.00');
      expect(CustomValidators.ncm(control)).toBeNull();
    });

    it('should return error for NCM with less than 8 digits', () => {
      const control = new FormControl('310100');
      expect(CustomValidators.ncm(control)).toEqual({ invalidNcm: true });
    });
  });

  describe('futureDate', () => {
    it('should return null for future date', () => {
      const tomorrow = new Date();
      tomorrow.setDate(tomorrow.getDate() + 5);
      const control = new FormControl(tomorrow.toISOString().split('T')[0]);
      expect(CustomValidators.futureDate(control)).toBeNull();
    });

    it('should return error for past date', () => {
      const control = new FormControl('2020-01-01');
      expect(CustomValidators.futureDate(control)).toEqual({ pastDate: true });
    });
  });

  describe('positiveNumber', () => {
    it('should return null for positive numbers', () => {
      expect(CustomValidators.positiveNumber(new FormControl(10.5))).toBeNull();
      expect(CustomValidators.positiveNumber(new FormControl('50.00'))).toBeNull();
    });

    it('should return error for zero or negative numbers', () => {
      expect(CustomValidators.positiveNumber(new FormControl(0))).toEqual({ notPositive: true });
      expect(CustomValidators.positiveNumber(new FormControl(-5))).toEqual({ notPositive: true });
    });
  });
});
