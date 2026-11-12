import { Pipe, PipeTransform } from '@angular/core';
import { MEASURE_UNIT_ABBREVIATIONS, MeasureUnit } from '../enums/measure-unit';

/**
 * Converte a enum MeasureUnit (ou valor equivalente) na sua abreviação em português (ex: Kg, un, l).
 */
@Pipe({
  name: 'measureUnitAbbr',
  standalone: true,
})
export class MeasureUnitAbbrPipe implements PipeTransform {
  private readonly fallbackMap: Record<string, string> = {
    KG: MEASURE_UNIT_ABBREVIATIONS[MeasureUnit.KILOGRAM],
    UN: MEASURE_UNIT_ABBREVIATIONS[MeasureUnit.UNIT],
    L: MEASURE_UNIT_ABBREVIATIONS[MeasureUnit.LITER],
  };

  transform(value: MeasureUnit | string | undefined | null): string {
    if (!value) return '-';
    const normalized = String(value).trim().toUpperCase();
    return (
      MEASURE_UNIT_ABBREVIATIONS[normalized as MeasureUnit] ??
      this.fallbackMap[normalized] ??
      String(value)
    );
  }
}

