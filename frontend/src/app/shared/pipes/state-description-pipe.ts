import { Pipe, PipeTransform } from '@angular/core';
import { BrazilianState, BrazilianStateCode } from '../enums/brazilian-state';

/** Converte a sigla da UF (ex.: 'PR') no nome por extenso (ex.: 'Paraná'). */
@Pipe({ name: 'stateDescription' })
export class StateDescriptionPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '-';
    return BrazilianState[value as BrazilianStateCode] ?? value;
  }
}
