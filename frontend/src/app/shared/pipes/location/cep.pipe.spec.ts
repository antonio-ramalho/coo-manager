import { describe, expect, it } from 'vitest';
import { CepPipe } from './cep.pipe';

describe('CepPipe', () => {
  const pipe = new CepPipe();

  it('deve formatar CEP de 8 dígitos', () => {
    expect(pipe.transform('85000000')).toBe('85000-000');
    expect(pipe.transform(85000000)).toBe('85000-000');
  });

  it('deve retornar "Sem CEP" para valores vazios', () => {
    expect(pipe.transform(null)).toBe('Sem CEP');
    expect(pipe.transform(undefined)).toBe('Sem CEP');
    expect(pipe.transform('')).toBe('Sem CEP');
  });

  it('deve retornar a string original se não tiver 8 dígitos', () => {
    expect(pipe.transform('1234')).toBe('1234');
  });
});
