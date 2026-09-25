import { AbstractControl, ValidationErrors } from '@angular/forms';

export class CustomValidators {
  static cpf(control: AbstractControl): ValidationErrors | null {
    const cpf = control.value;
    if (!cpf) return null;

    const numbers = cpf.replace(/\D/g, '');
    if (numbers.length !== 11 || /^(\d)\1{10}$/.test(numbers)) {
      return { invalidCpf: true };
    }

    let sum = 0;
    let remainder;
    for (let i = 1; i <= 9; i++) sum = sum + parseInt(numbers.substring(i - 1, i)) * (11 - i);
    remainder = (sum * 10) % 11;
    if (remainder === 10 || remainder === 11) remainder = 0;
    if (remainder !== parseInt(numbers.substring(9, 10))) return { invalidCpf: true };

    sum = 0;
    for (let i = 1; i <= 10; i++) sum = sum + parseInt(numbers.substring(i - 1, i)) * (12 - i);
    remainder = (sum * 10) % 11;
    if (remainder === 10 || remainder === 11) remainder = 0;
    if (remainder !== parseInt(numbers.substring(10, 11))) return { invalidCpf: true };

    return null;
  }

  static birthDate(control: AbstractControl): ValidationErrors | null {
    const data = control.value;
    if (!data) return null;

    const dataNascimento = new Date(data + 'T00:00:00');
    const hoje = new Date();

    let idade = hoje.getFullYear() - dataNascimento.getFullYear();
    const diferencaMeses = hoje.getMonth() - dataNascimento.getMonth();

    if (diferencaMeses < 0 || (diferencaMeses === 0 && hoje.getDate() < dataNascimento.getDate())) {
      idade--;
    }

    if (idade < 18) {
      return { minor: true };
    }

    return null;
  }

  static cnpj(control: AbstractControl): ValidationErrors | null {
    const cnpjStr = control.value;

    if (!cnpjStr) return null;

    const numbers = cnpjStr.replace(/\D/g, '');

    if (numbers.length !== 14 || /^(\d)\1{13}$/.test(numbers)) {
      return { invalidCnpj: true };
    }

    let size = numbers.length - 2;
    let base = numbers.substring(0, size);
    const digits = numbers.substring(size);
    let sum = 0;
    let pos = size - 7;

    for (let i = size; i >= 1; i--) {
      sum += parseInt(base.charAt(size - i)) * pos--;
      if (pos < 2) pos = 9;
    }

    let resultado = sum % 11 < 2 ? 0 : 11 - (sum % 11);
    if (resultado !== parseInt(digits.charAt(0))) {
      return { invalidCnpj: true };
    }

    size = size + 1;
    base = numbers.substring(0, size);
    sum = 0;
    pos = size - 7;

    for (let i = size; i >= 1; i--) {
      sum += parseInt(base.charAt(size - i)) * pos--;
      if (pos < 2) pos = 9;
    }

    resultado = sum % 11 < 2 ? 0 : 11 - (sum % 11);
    if (resultado !== parseInt(digits.charAt(1))) {
      return { invalidCnpj: true };
    }
    return null;
  }
}
