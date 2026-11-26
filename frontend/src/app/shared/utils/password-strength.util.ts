import { PasswordStrengthLevel } from '../enums/password-strength.enum';

export interface PasswordStrengthResult {
  level: PasswordStrengthLevel;
  score: number;
  label: string;
  colorClass: string;
  percentage: number;
  hasMinLength: boolean;
  hasMixedCase: boolean;
  hasNumber: boolean;
  hasSpecialChar: boolean;
}

export function evaluatePasswordStrength(password: string): PasswordStrengthResult {
  if (!password) {
    return {
      level: PasswordStrengthLevel.WEAK,
      score: 0,
      label: 'Fraca',
      colorClass: 'strength-weak',
      percentage: 0,
      hasMinLength: false,
      hasMixedCase: false,
      hasNumber: false,
      hasSpecialChar: false,
    };
  }

  const hasMinLength = password.length >= 6;
  const hasMixedCase = /[a-z]/.test(password) && /[A-Z]/.test(password);
  const hasNumber = /\d/.test(password);
  const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

  let criteriaCount = 0;
  if (hasMinLength) criteriaCount++;
  if (hasMixedCase) criteriaCount++;
  if (hasNumber) criteriaCount++;
  if (hasSpecialChar) criteriaCount++;
  if (password.length >= 10) criteriaCount++;

  let level: PasswordStrengthLevel;
  let label: string;
  let colorClass: string;
  let percentage: number;

  if (criteriaCount <= 2 || !hasMinLength) {
    level = PasswordStrengthLevel.WEAK;
    label = 'Fraca';
    colorClass = 'strength-weak';
    percentage = 33;
  } else if (criteriaCount <= 4) {
    level = PasswordStrengthLevel.MEDIUM;
    label = 'Média';
    colorClass = 'strength-medium';
    percentage = 66;
  } else {
    level = PasswordStrengthLevel.STRONG;
    label = 'Forte';
    colorClass = 'strength-strong';
    percentage = 100;
  }

  return {
    level,
    score: criteriaCount,
    label,
    colorClass,
    percentage,
    hasMinLength,
    hasMixedCase,
    hasNumber,
    hasSpecialChar,
  };
}
