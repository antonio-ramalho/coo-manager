import { Pipe, PipeTransform } from '@angular/core';
import { PaymentStatus, PAYMENT_STATUS_LABELS } from '../../enums/payment-status.enum';

@Pipe({
  name: 'paymentStatus',
  standalone: true,
})
export class PaymentStatusPipe implements PipeTransform {
  transform(value: PaymentStatus | string | null | undefined): string {
    if (!value) return 'Não informado';

    const normalizedStatus = value.toUpperCase() as PaymentStatus;
    return PAYMENT_STATUS_LABELS[normalizedStatus] || value;
  }
}
