const CPF_LENGTH = 11;
const CNPJ_LENGTH = 14;

/**
 * Formata um CPF (000.000.000-00) ou CNPJ (00.000.000/0000-00) conforme a quantidade de dígitos.
 * Valores com tamanho inesperado são devolvidos sem alteração.
 */
export function formatDocument(value: string | null | undefined): string {
  if (!value) return '';

  const digits = value.replace(/\D/g, '');

  if (digits.length === CPF_LENGTH) {
    return digits.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  if (digits.length === CNPJ_LENGTH) {
    return digits.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
  }

  return value;
}
