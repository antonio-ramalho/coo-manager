import { describe, expect, it } from 'vitest';
import { PhonePipe } from './phone.pipe';

describe('PhonePipe', () => {
  const pipe = new PhonePipe();

  it('deve formatar telefone celular (11 dígitos)', () => {
    expect(pipe.transform('42999998888')).toBe('(42) 99999-8888');
  });

  it('deve formatar telefone fixo (10 dígitos)', () => {
    expect(pipe.transform('4236221122')).toBe('(42) 3622-1122');
  });

  it('deve retornar "Sem telefone" para valores nulos ou vazios', () => {
    expect(pipe.transform(null)).toBe('Sem telefone');
    expect(pipe.transform(undefined)).toBe('Sem telefone');
    expect(pipe.transform('')).toBe('Sem telefone');
  });

  it('deve retornar a string original se o tamanho for inesperado', () => {
    expect(pipe.transform('123')).toBe('123');
  });
});
