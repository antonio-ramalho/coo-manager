import { describe, expect, it } from 'vitest';
import { NameFormatPipe } from './name-format.pipe';

describe('NameFormatPipe', () => {
  const pipe = new NameFormatPipe();

  it('deve capitalizar nomes próprios preservando preposições', () => {
    expect(pipe.transform('joão da silva')).toBe('João da Silva');
    expect(pipe.transform('maria de souza e santos')).toBe('Maria de Souza e Santos');
  });

  it('deve truncar quando exceder maxLength', () => {
    const longName = 'Sebastião Francisco de Paula Albuquerque';
    expect(pipe.transform(longName, 20)).toBe('Sebastião Francisco...');
  });

  it('deve retornar string vazia para valores nulos ou vazios', () => {
    expect(pipe.transform(null)).toBe('');
    expect(pipe.transform(undefined)).toBe('');
    expect(pipe.transform('')).toBe('');
  });
});
