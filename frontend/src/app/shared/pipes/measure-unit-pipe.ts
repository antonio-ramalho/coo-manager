import { Pipe, PipeTransform } from '@angular/core';
import { MeasureUnit } from '../enums/measure-unit';

@Pipe({
  name: 'measureUnitLabel',
  standalone: true,
})
export class MeasureUnitPipe implements PipeTransform {
  private readonly unitMap: Record<string, string> = {
    [MeasureUnit.KILOGRAM]: 'Quilograma',
    [MeasureUnit.UNIT]: 'Unidade',
    [MeasureUnit.LITER]: 'Litro',
  };

  transform(value: MeasureUnit | string | undefined | null): string {
    if (!value) return '-';
    return this.unitMap[value] || value;
  }
}
