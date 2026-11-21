import { describe, expect, it } from 'vitest';
import { ReceiptCodePipe } from './receipt-code.pipe';

describe('ReceiptCodePipe', () => {
  const pipe = new ReceiptCodePipe();

  it('deve formatar UUID em código legível com prefixo padrão', () => {
    expect(pipe.transform('c9a0d8e1-550e-4840-8c29-4b3e8e2d4f1a')).toBe('VND-C9A0D8E1');
  });

  it('deve permitir prefixo customizado', () => {
    expect(pipe.transform('c9a0d8e1-550e-4840-8c29-4b3e8e2d4f1a', 'PED')).toBe('PED-C9A0D8E1');
  });

  it('deve retornar "—" quando o uuid for nulo ou vazio', () => {
    expect(pipe.transform(null)).toBe('—');
    expect(pipe.transform(undefined)).toBe('—');
    expect(pipe.transform('   ')).toBe('—');
  });
});
