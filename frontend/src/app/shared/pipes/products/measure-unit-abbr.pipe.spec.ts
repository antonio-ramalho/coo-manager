import { describe, expect, it } from 'vitest';
import { MeasureUnitAbbrPipe } from './measure-unit-abbr.pipe';
import { MeasureUnit } from '../../enums/measure-unit';

describe('MeasureUnitAbbrPipe', () => {
  const pipe = new MeasureUnitAbbrPipe();

  it('deve retornar a abreviação da unidade', () => {
    expect(pipe.transform(MeasureUnit.KILOGRAM)).toBe('Kg');
    expect(pipe.transform(MeasureUnit.UNIT)).toBe('un');
    expect(pipe.transform(MeasureUnit.LITER)).toBe('l');
  });

  it('deve suportar strings equivalentes normalizadas', () => {
    expect(pipe.transform('kg')).toBe('Kg');
    expect(pipe.transform('un')).toBe('un');
    expect(pipe.transform('l')).toBe('l');
  });

  it('deve retornar "-" para valores nulos ou vazios', () => {
    expect(pipe.transform(null)).toBe('-');
    expect(pipe.transform(undefined)).toBe('-');
    expect(pipe.transform('')).toBe('-');
  });
});
