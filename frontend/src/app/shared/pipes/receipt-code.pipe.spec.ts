import { describe, expect, it } from 'vitest';
import { ReceiptCodePipe } from './receipt-code.pipe';

describe('ReceiptCodePipe', () => {
  const pipe = new ReceiptCodePipe();

  it('should format a valid UUID with default VND prefix', () => {
    expect(pipe.transform('550e8400-e29b-41d4-a716-446655440000')).toBe('VND-550E8400');
  });

  it('should support a custom prefix', () => {
    expect(pipe.transform('a1b2c3d4-0000-0000-0000-000000000000', 'REC')).toBe('REC-A1B2C3D4');
  });

  it('should return em dash when uuid is empty or null', () => {
    expect(pipe.transform(null)).toBe('—');
    expect(pipe.transform(undefined)).toBe('—');
    expect(pipe.transform('   ')).toBe('—');
  });
});

