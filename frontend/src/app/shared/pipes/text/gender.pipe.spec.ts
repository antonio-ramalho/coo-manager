import { describe, expect, it } from 'vitest';
import { GenderPipe } from './gender.pipe';
import { Gender } from '../../enums/gender';

describe('GenderPipe', () => {
  const pipe = new GenderPipe();

  it('deve retornar a descrição dos gêneros', () => {
    expect(pipe.transform(Gender.MALE)).toBe('Masculino');
    expect(pipe.transform(Gender.FEMALE)).toBe('Feminino');
    expect(pipe.transform(Gender.OTHER)).toBe('Outro');
    expect(pipe.transform(Gender.NOT_SAY)).toBe('Prefiro não dizer');
  });

  it('deve retornar "Não informado" quando valor for nulo ou vazio', () => {
    expect(pipe.transform(null)).toBe('Não informado');
    expect(pipe.transform(undefined)).toBe('Não informado');
  });

  it('deve retornar "Desconhecido" para valores desconhecidos', () => {
    expect(pipe.transform('OUTRO_GENERO')).toBe('Desconhecido');
  });
});
