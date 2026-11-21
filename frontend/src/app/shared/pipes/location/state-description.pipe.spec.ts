import { describe, expect, it } from 'vitest';
import { StateDescriptionPipe } from './state-description.pipe';

describe('StateDescriptionPipe', () => {
  const pipe = new StateDescriptionPipe();

  it('deve converter sigla da UF no nome do Estado por extenso', () => {
    expect(pipe.transform('PR')).toBe('Paraná');
    expect(pipe.transform('SP')).toBe('São Paulo');
    expect(pipe.transform('SC')).toBe('Santa Catarina');
  });

  it('deve retornar "-" quando valor for vazio', () => {
    expect(pipe.transform(null)).toBe('-');
    expect(pipe.transform(undefined)).toBe('-');
    expect(pipe.transform('')).toBe('-');
  });

  it('deve retornar o valor original caso a sigla não seja mapeada', () => {
    expect(pipe.transform('XX')).toBe('XX');
  });
});
