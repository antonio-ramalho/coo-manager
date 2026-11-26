import { Pipe, PipeTransform } from '@angular/core';
import { BrazilianState, BrazilianStateCode } from '../../enums/brazilian-state';

@Pipe({
  name: 'stateDescription',
  standalone: true,
})
export class StateDescriptionPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '-';
    return BrazilianState[value as BrazilianStateCode] ?? value;
  }
}
