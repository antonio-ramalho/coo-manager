export const onlyDigits = (value: string): string => value.replace(/\D/g, '');
export const hasAllSameDigits = (digits: string): boolean => /^(\d)\1+$/.test(digits);

export const cpfCheckDigit = (base: string, initialWeight: number): number => {
  const sum = [...base].reduce((acc, digit, index) => acc + Number(digit) * (initialWeight - index), 0);
  const remainder = (sum * 10) % 11;
  return remainder === 10 ? 0 : remainder;
};

export const cnpjCheckDigit = (base: string): number => {
  const sum = [...base]
    .reverse()
    .reduce((acc, digit, index) => acc + Number(digit) * ((index % 8) + 2), 0);
  const remainder = sum % 11;
  return remainder < 2 ? 0 : 11 - remainder;
};
