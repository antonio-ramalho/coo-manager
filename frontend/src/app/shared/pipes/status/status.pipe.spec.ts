import { describe, expect, it } from 'vitest';
import { StatusPipe } from './status.pipe';
import { Status } from '../../enums/status';

describe('StatusPipe', () => {
  const pipe = new StatusPipe();

  it('deve formatar status conhecidos', () => {
    expect(pipe.transform(Status.ACTIVE)).toBe('Ativo');
    expect(pipe.transform(Status.INACTIVE)).toBe('Inativo');
    expect(pipe.transform(Status.SUSPENDED)).toBe('Suspenso');
  });

  it('deve retornar "Desconhecido" para valores nulos, vazios ou desconhecidos', () => {
    expect(pipe.transform(null)).toBe('Desconhecido');
    expect(pipe.transform(undefined)).toBe('Desconhecido');
    expect(pipe.transform('OUTRO')).toBe('Desconhecido');
  });
});
