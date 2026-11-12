import { describe, expect, it } from 'vitest';
import { MeasureUnit } from '../enums/measure-unit';
import { MeasureUnitAbbrPipe } from './measure-unit-abbr.pipe';

describe('MeasureUnitAbbrPipe', () => {
  const pipe = new MeasureUnitAbbrPipe();

  it('should map MeasureUnit enum values to Portuguese abbreviations', () => {
    expect(pipe.transform(MeasureUnit.KILOGRAM)).toBe('Kg');
    expect(pipe.transform(MeasureUnit.UNIT)).toBe('un');
    expect(pipe.transform(MeasureUnit.LITER)).toBe('l');
  });

  it('should normalize shorthand strings and fallback cleanly on empty values', () => {
    expect(pipe.transform('KG')).toBe('Kg');
    expect(pipe.transform('UN')).toBe('un');
    expect(pipe.transform('l')).toBe('l');
    expect(pipe.transform(null)).toBe('-');
    expect(pipe.transform(undefined)).toBe('-');
  });
});

