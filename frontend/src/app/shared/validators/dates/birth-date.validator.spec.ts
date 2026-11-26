import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { birthDateValidator } from './birth-date.validator';

describe('birthDateValidator', () => {
  it('deve aceitar nulo ou vazio', () => {
    expect(birthDateValidator(new FormControl(null))).toBeNull();
    expect(birthDateValidator(new FormControl(''))).toBeNull();
  });

  it('deve aceitar data de maior de idade', () => {
    const adultYear = new Date().getFullYear() - 25;
    expect(birthDateValidator(new FormControl(`${adultYear}-01-15`))).toBeNull();
  });

  it('deve rejeitar data de menor de 18 anos', () => {
    const minorYear = new Date().getFullYear() - 10;
    expect(birthDateValidator(new FormControl(`${minorYear}-01-15`))).toEqual({ minor: true });
  });
});
