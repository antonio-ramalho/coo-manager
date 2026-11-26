import { AbstractControl, ValidationErrors } from '@angular/forms';
import { cnpjCheckDigit, hasAllSameDigits, onlyDigits } from '../helpers/document-digits.helper';

export function cnpjValidator(control: AbstractControl<string | null>): ValidationErrors | null {
  if (!control.value) return null;

  const digits = onlyDigits(control.value);
  if (digits.length !== 14 || hasAllSameDigits(digits)) return { invalidCnpj: true };

  const firstDigit = cnpjCheckDigit(digits.substring(0, 12));
  const secondDigit = cnpjCheckDigit(digits.substring(0, 13));
  const isValid = firstDigit === Number(digits[12]) && secondDigit === Number(digits[13]);

  return isValid ? null : { invalidCnpj: true };
}
