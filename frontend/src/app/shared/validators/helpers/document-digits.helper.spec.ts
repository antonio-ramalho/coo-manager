import { describe, expect, it } from 'vitest';
import {
  onlyDigits,
  hasAllSameDigits,
  cpfCheckDigit,
  cnpjCheckDigit,
} from './document-digits.helper';

describe('documentDigitsHelper', () => {
  it('onlyDigits deve remover caracteres não numéricos', () => {
    expect(onlyDigits('123.456.789-00')).toBe('12345678900');
    expect(onlyDigits('abc-12/34')).toBe('1234');
    expect(onlyDigits('')).toBe('');
  });

  it('hasAllSameDigits deve detectar sequências idênticas', () => {
    expect(hasAllSameDigits('11111111111')).toBe(true);
    expect(hasAllSameDigits('00000000000000')).toBe(true);
    expect(hasAllSameDigits('12345678901')).toBe(false);
  });

  it('cpfCheckDigit deve calcular dígitos verificadores de CPF corretamente', () => {
    const base = '529982247';
    const firstDigit = cpfCheckDigit(base, 10);
    const secondDigit = cpfCheckDigit(base + firstDigit, 11);
    expect(firstDigit).toBe(2);
    expect(secondDigit).toBe(5);
  });

  it('cnpjCheckDigit deve calcular dígitos verificadores de CNPJ corretamente', () => {
    const base = '112223330001';
    const firstDigit = cnpjCheckDigit(base);
    const secondDigit = cnpjCheckDigit(base + firstDigit);
    expect(firstDigit).toBe(8);
    expect(secondDigit).toBe(1);
  });
});
