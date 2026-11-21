import { describe, expect, it } from 'vitest';
import { PaymentStatusPipe } from './payment-status.pipe';
import { PaymentStatus } from '../../enums/payment-status.enum';

describe('PaymentStatusPipe', () => {
  const pipe = new PaymentStatusPipe();

  it('deve retornar "Pago" para PAID', () => {
    expect(pipe.transform(PaymentStatus.PAID)).toBe('Pago');
    expect(pipe.transform('paid')).toBe('Pago');
  });

  it('deve retornar "Pendente" para PENDING', () => {
    expect(pipe.transform(PaymentStatus.PENDING)).toBe('Pendente');
  });

  it('deve retornar "Cancelado" para CANCELED', () => {
    expect(pipe.transform(PaymentStatus.CANCELED)).toBe('Cancelado');
  });

  it('deve retornar "Vencido" para COMPLETED', () => {
    expect(pipe.transform(PaymentStatus.COMPLETED)).toBe('Vencido');
  });

  it('deve retornar "Não informado" para valores nulos ou vazios', () => {
    expect(pipe.transform(null)).toBe('Não informado');
    expect(pipe.transform(undefined)).toBe('Não informado');
    expect(pipe.transform('')).toBe('Não informado');
  });

  it('deve retornar o próprio valor caso não encontre correspondência', () => {
    expect(pipe.transform('CUSTOM_STATUS')).toBe('CUSTOM_STATUS');
  });
});
