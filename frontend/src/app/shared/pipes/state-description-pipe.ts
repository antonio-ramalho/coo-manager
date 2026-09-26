import { Pipe, PipeTransform } from '@angular/core';
import { BrazilianStatesMap } from '../utils/BrazilianStatesMap';

@Pipe({
  name: 'stateDescription',
  standalone: true,
})
export class StateDescriptionPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '-';
    return BrazilianStatesMap[value] || value;
  }
}
