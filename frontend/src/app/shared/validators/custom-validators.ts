import { AbstractControl, ValidationErrors } from '@angular/forms';

const MINIMUM_AGE = 18;

/** Remove tudo que não for dígito. */
const onlyDigits = (value: string): string => value.replace(/\D/g, '');

/** Verdadeiro quando todos os dígitos são iguais (ex.: 111.111.111-11), caso inválido para CPF/CNPJ. */
const hasAllSameDigits = (digits: string): boolean => /^(\d)\1+$/.test(digits);

/** Calcula um dígito verificador de CPF a partir dos dígitos base e do peso inicial. */
const cpfCheckDigit = (base: string, initialWeight: number): number => {
  const sum = [...base].reduce((acc, digit, index) => acc + Number(digit) * (initialWeight - index), 0);
  const remainder = (sum * 10) % 11;
  return remainder === 10 ? 0 : remainder;
};

/** Calcula um dígito verificador de CNPJ (pesos cíclicos de 2 a 9, da direita para a esquerda). */
const cnpjCheckDigit = (base: string): number => {
  const sum = [...base]
    .reverse()
    .reduce((acc, digit, index) => acc + Number(digit) * ((index % 8) + 2), 0);
  const remainder = sum % 11;
  return remainder < 2 ? 0 : 11 - remainder;
};

/** Calcula a idade completa em anos na data de hoje. */
const ageInYears = (birthDate: Date, today: Date = new Date()): number => {
  const age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  const birthdayNotReached = monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate());
  return birthdayNotReached ? age - 1 : age;
};

/** Validadores reutilizáveis de Reactive Forms para documentos e datas brasileiras. */
export class CustomValidators {
  /** Valida CPF (11 dígitos + dígitos verificadores). Valores vazios ficam a cargo de `Validators.required`. */
  static cpf(control: AbstractControl<string | null>): ValidationErrors | null {
    if (!control.value) return null;

    const digits = onlyDigits(control.value);
    if (digits.length !== 11 || hasAllSameDigits(digits)) return { invalidCpf: true };

    const firstDigit = cpfCheckDigit(digits.substring(0, 9), 10);
    const secondDigit = cpfCheckDigit(digits.substring(0, 10), 11);
    const isValid = firstDigit === Number(digits[9]) && secondDigit === Number(digits[10]);

    return isValid ? null : { invalidCpf: true };
  }

  /** Valida CNPJ (14 dígitos + dígitos verificadores). */
  static cnpj(control: AbstractControl<string | null>): ValidationErrors | null {
    if (!control.value) return null;

    const digits = onlyDigits(control.value);
    if (digits.length !== 14 || hasAllSameDigits(digits)) return { invalidCnpj: true };

    const firstDigit = cnpjCheckDigit(digits.substring(0, 12));
    const secondDigit = cnpjCheckDigit(digits.substring(0, 13));
    const isValid = firstDigit === Number(digits[12]) && secondDigit === Number(digits[13]);

    return isValid ? null : { invalidCnpj: true };
  }

  /** Exige idade mínima de 18 anos. Espera a data no formato ISO `YYYY-MM-DD`. */
  static birthDate(control: AbstractControl<string | null>): ValidationErrors | null {
    if (!control.value) return null;

    const birthDate = new Date(`${control.value}T00:00:00`);
    return ageInYears(birthDate) < MINIMUM_AGE ? { minor: true } : null;
  }

  /** Valida código NCM brasileiro (8 dígitos numéricos). */
  static ncm(control: AbstractControl<string | null>): ValidationErrors | null {
    if (!control.value) return null;

    const digits = onlyDigits(control.value);
    if (digits.length !== 8) return { invalidNcm: true };

    return null;
  }

  /** Valida se a data de validade é posterior à data atual (data futura). */
  static futureDate(control: AbstractControl<string | null>): ValidationErrors | null {
    if (!control.value) return null;

    let targetDate: Date;
    if (control.value.includes('/')) {
      const parts = control.value.split('/');
      if (parts.length === 3) {
        targetDate = new Date(Number(parts[2]), Number(parts[1]) - 1, Number(parts[0]));
      } else {
        return { pastDate: true };
      }
    } else {
      targetDate = new Date(`${control.value}T00:00:00`);
    }

    if (isNaN(targetDate.getTime())) return { pastDate: true };

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    return targetDate <= today ? { pastDate: true } : null;
  }

  /** Valida se o número é estritamente positivo (> 0). */
  static positiveNumber(control: AbstractControl<number | string | null>): ValidationErrors | null {
    if (control.value === null || control.value === undefined || control.value === '') return null;

    const numericValue = typeof control.value === 'string'
      ? parseFloat(control.value.replace(',', '.'))
      : Number(control.value);

    if (isNaN(numericValue) || numericValue <= 0) {
      return { notPositive: true };
    }

    return null;
  }
}
