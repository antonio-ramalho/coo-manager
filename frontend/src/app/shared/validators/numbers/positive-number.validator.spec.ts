import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { positiveNumberValidator } from './positive-number.validator';

describe('positiveNumberValidator', () => {
  it('deve aceitar nulo, indefinido ou vazio', () => {
    expect(positiveNumberValidator(new FormControl<number | string | null | undefined>(null))).toBeNull();
    expect(positiveNumberValidator(new FormControl<number | string | null | undefined>(undefined))).toBeNull();
    expect(positiveNumberValidator(new FormControl(''))).toBeNull();
  });

  it('deve aceitar número positivo', () => {
    expect(positiveNumberValidator(new FormControl(10))).toBeNull();
    expect(positiveNumberValidator(new FormControl('15.5'))).toBeNull();
    expect(positiveNumberValidator(new FormControl('15,5'))).toBeNull();
  });

  it('deve rejeitar zero ou número negativo', () => {
    expect(positiveNumberValidator(new FormControl(0))).toEqual({ notPositive: true });
    expect(positiveNumberValidator(new FormControl(-5))).toEqual({ notPositive: true });
    expect(positiveNumberValidator(new FormControl('0'))).toEqual({ notPositive: true });
    expect(positiveNumberValidator(new FormControl('-10.2'))).toEqual({ notPositive: true });
  });

  it('deve rejeitar texto não numérico', () => {
    expect(positiveNumberValidator(new FormControl('abc'))).toEqual({ notPositive: true });
  });
});
