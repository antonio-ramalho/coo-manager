import { Pipe, PipeTransform } from '@angular/core';

/**
 * Formata um UUID de recibo/venda em um código curto legível para exibição (ex: VND-550E8400).
 */
@Pipe({
  name: 'receiptCode',
  standalone: true,
})
export class ReceiptCodePipe implements PipeTransform {
  transform(uuid: string | null | undefined, prefix: string = 'VND'): string {
    if (!uuid || !uuid.trim()) {
      return '—';
    }
    const shortSegment = uuid.trim().replace(/-/g, '').substring(0, 8).toUpperCase();
    return `${prefix}-${shortSegment}`;
  }
}

