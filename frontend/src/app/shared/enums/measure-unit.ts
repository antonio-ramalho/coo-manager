export enum MeasureUnit {
  KILOGRAM = 'KILOGRAM',
  UNIT = 'UNIT',
  LITER = 'LITER',
}

/**
 * Mapeamento de abreviações padronizadas para exibição compacta das unidades de medida em português.
 */
export const MEASURE_UNIT_ABBREVIATIONS: Record<MeasureUnit, string> = {
  [MeasureUnit.KILOGRAM]: 'Kg',
  [MeasureUnit.UNIT]: 'un',
  [MeasureUnit.LITER]: 'l',
};
