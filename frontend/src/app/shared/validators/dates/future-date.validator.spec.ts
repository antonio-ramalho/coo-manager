import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { futureDateValidator } from './future-date.validator';

describe('futureDateValidator', () => {
  it('deve aceitar nulo ou vazio', () => {
    expect(futureDateValidator(new FormControl(null))).toBeNull();
    expect(futureDateValidator(new FormControl(''))).toBeNull();
  });

  it('deve aceitar data futura (ISO)', () => {
    const futureYear = new Date().getFullYear() + 2;
    expect(futureDateValidator(new FormControl(`${futureYear}-12-31`))).toBeNull();
  });

  it('deve aceitar data futura (DD/MM/YYYY)', () => {
    const futureYear = new Date().getFullYear() + 2;
    expect(futureDateValidator(new FormControl(`31/12/${futureYear}`))).toBeNull();
  });

  it('deve rejeitar data passada ou de hoje', () => {
    const pastYear = new Date().getFullYear() - 1;
    expect(futureDateValidator(new FormControl(`${pastYear}-01-01`))).toEqual({ pastDate: true });
  });

  it('deve rejeitar data inválida', () => {
    expect(futureDateValidator(new FormControl('data-invalida'))).toEqual({ pastDate: true });
  });
});
