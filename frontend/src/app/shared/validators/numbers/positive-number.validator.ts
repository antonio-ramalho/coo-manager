import { AbstractControl, ValidationErrors } from '@angular/forms';

export function positiveNumberValidator(control: AbstractControl<number | string | null | undefined>): ValidationErrors | null {
  if (control.value === null || control.value === undefined || control.value === '') return null;

  const numericValue = typeof control.value === 'string'
    ? parseFloat(control.value.replace(',', '.'))
    : Number(control.value);

  if (isNaN(numericValue) || numericValue <= 0) {
    return { notPositive: true };
  }

  return null;
}
