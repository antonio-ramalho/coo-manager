import { AbstractControl, ValidationErrors } from '@angular/forms';
import { cpfCheckDigit, hasAllSameDigits, onlyDigits } from '../helpers/document-digits.helper';

export function cpfValidator(control: AbstractControl<string | null>): ValidationErrors | null {
  if (!control.value) return null;

  const digits = onlyDigits(control.value);
  if (digits.length !== 11 || hasAllSameDigits(digits)) return { invalidCpf: true };

  const firstDigit = cpfCheckDigit(digits.substring(0, 9), 10);
  const secondDigit = cpfCheckDigit(digits.substring(0, 10), 11);
  const isValid = firstDigit === Number(digits[9]) && secondDigit === Number(digits[10]);

  return isValid ? null : { invalidCpf: true };
}
