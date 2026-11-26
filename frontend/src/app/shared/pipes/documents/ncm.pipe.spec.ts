import { describe, expect, it } from 'vitest';
import { NcmPipe } from './ncm.pipe';

describe('NcmPipe', () => {
  const pipe = new NcmPipe();

  it('deve formatar NCM de 8 dígitos no padrão 0000.00.00', () => {
    expect(pipe.transform('12345678')).toBe('1234.56.78');
    expect(pipe.transform('1234.56.78')).toBe('1234.56.78');
  });

  it('deve retornar "-" para valores nulos ou vazios', () => {
    expect(pipe.transform(null)).toBe('-');
    expect(pipe.transform(undefined)).toBe('-');
    expect(pipe.transform('')).toBe('-');
  });

  it('deve retornar o valor original se não tiver 8 dígitos', () => {
    expect(pipe.transform('12345')).toBe('12345');
  });
});
