import { FormControl } from '@angular/forms';
import { describe, expect, it } from 'vitest';
import { ncmValidator } from './ncm.validator';

describe('ncmValidator', () => {
  it('deve aceitar nulo ou vazio', () => {
    expect(ncmValidator(new FormControl(null))).toBeNull();
    expect(ncmValidator(new FormControl(''))).toBeNull();
  });

  it('deve aceitar NCM de 8 dígitos', () => {
    expect(ncmValidator(new FormControl('12345678'))).toBeNull();
    expect(ncmValidator(new FormControl('1234.56.78'))).toBeNull();
  });

  it('deve rejeitar NCM com quantidade diferente de 8 dígitos', () => {
    expect(ncmValidator(new FormControl('1234567'))).toEqual({ invalidNcm: true });
    expect(ncmValidator(new FormControl('123456789'))).toEqual({ invalidNcm: true });
  });
});
