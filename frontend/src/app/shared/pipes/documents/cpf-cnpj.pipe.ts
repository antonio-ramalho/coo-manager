import { Pipe, PipeTransform } from '@angular/core';
import { formatDocument } from '../../utils/document-format';

@Pipe({
  name: 'cpfCnpj',
  standalone: true,
})
export class CpfCnpjPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    return formatDocument(value);
  }
}
