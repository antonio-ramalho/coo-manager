
export enum PaymentStatus {
  PAID = 'PAID',
  PENDING = 'PENDING',
  CANCELED = 'CANCELED',
  COMPLETED = 'COMPLETED',
}

export const PAYMENT_STATUS_LABELS: Record<PaymentStatus, string> = {
  [PaymentStatus.PAID]: 'Pago',
  [PaymentStatus.PENDING]: 'Pendente',
  [PaymentStatus.CANCELED]: 'Cancelado',
  [PaymentStatus.COMPLETED]: 'Vencido',
};

export const PAYMENT_STATUS_CLASSES: Record<PaymentStatus, string> = {
  [PaymentStatus.PAID]: 'status-paid',
  [PaymentStatus.PENDING]: 'status-pending',
  [PaymentStatus.CANCELED]: 'status-canceled',
  [PaymentStatus.COMPLETED]: 'status-completed',
};
