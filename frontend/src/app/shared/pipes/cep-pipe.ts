import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cep',
})
export class CepPipe implements PipeTransform {
  transform(value: string | number | null | undefined): string {
    if (!value) return 'Sem CEP';

    const cleaned = value.toString().replace(/\D/g, '');

    if (cleaned.length === 8) {
      return `${cleaned.substring(0, 5)}-${cleaned.substring(5)}`;
    }

    return value.toString();
  }
}
