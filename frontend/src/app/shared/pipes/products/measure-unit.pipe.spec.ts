import { describe, expect, it } from 'vitest';
import { MeasureUnitPipe } from './measure-unit.pipe';
import { MeasureUnit } from '../../enums/measure-unit';

describe('MeasureUnitPipe', () => {
  const pipe = new MeasureUnitPipe();

  it('deve retornar a descrição por extenso das unidades conhecidas', () => {
    expect(pipe.transform(MeasureUnit.KILOGRAM)).toBe('Quilograma');
    expect(pipe.transform(MeasureUnit.UNIT)).toBe('Unidade');
    expect(pipe.transform(MeasureUnit.LITER)).toBe('Litro');
  });

  it('deve retornar "-" quando valor for nulo ou indefinido', () => {
    expect(pipe.transform(null)).toBe('-');
    expect(pipe.transform(undefined)).toBe('-');
  });

  it('deve retornar a string original se não estiver no mapa', () => {
    expect(pipe.transform('TONELADA')).toBe('TONELADA');
  });
});
