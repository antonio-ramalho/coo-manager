const CPF_LENGTH = 11;
const CNPJ_LENGTH = 14;

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
