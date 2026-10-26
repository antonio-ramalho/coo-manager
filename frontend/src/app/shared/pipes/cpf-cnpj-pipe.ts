import { Pipe, PipeTransform } from '@angular/core';
import { formatDocument } from '../utils/document-format';

/** Formata CPF ou CNPJ no template. A lógica fica em `formatDocument`, reutilizável fora dos templates. */
@Pipe({ name: 'cpfCnpj' })
export class CpfCnpjPipe implements PipeTransform {
  transform(value: string | null | undefined): string {
    return formatDocument(value);
  }
}
