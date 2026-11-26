import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'ncm',
  standalone: true,
})
export class NcmPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    if (!value) return '-';
    const digits = value.replace(/\D/g, '');
    if (digits.length === 8) {
      return `${digits.slice(0, 4)}.${digits.slice(4, 6)}.${digits.slice(6, 8)}`;
    }
    return value;
  }
}
