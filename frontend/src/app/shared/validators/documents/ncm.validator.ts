import { AbstractControl, ValidationErrors } from '@angular/forms';
import { onlyDigits } from '../helpers/document-digits.helper';

export function ncmValidator(control: AbstractControl<string | null>): ValidationErrors | null {
  if (!control.value) return null;

  const digits = onlyDigits(control.value);
  if (digits.length !== 8) return { invalidNcm: true };

  return null;
}
