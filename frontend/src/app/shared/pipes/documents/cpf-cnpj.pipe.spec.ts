import { describe, expect, it } from 'vitest';
import { CpfCnpjPipe } from './cpf-cnpj.pipe';

describe('CpfCnpjPipe', () => {
  const pipe = new CpfCnpjPipe();

  it('deve formatar CPF válido com máscara', () => {
    expect(pipe.transform('12345678901')).toBe('123.456.789-01');
  });

  it('deve formatar CNPJ válido com máscara', () => {
    expect(pipe.transform('11222333000181')).toBe('11.222.333/0001-81');
  });

  it('deve retornar string vazia ou valor original para entradas vazias/inválidas', () => {
    expect(pipe.transform(null)).toBe('');
    expect(pipe.transform(undefined)).toBe('');
    expect(pipe.transform('')).toBe('');
    expect(pipe.transform('123')).toBe('123');
  });
});
