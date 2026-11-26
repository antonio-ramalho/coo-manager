import { AbstractControl, ValidationErrors } from '@angular/forms';

const MINIMUM_AGE = 18;

export const ageInYears = (birthDate: Date, today: Date = new Date()): number => {
  const age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  const birthdayNotReached = monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate());
  return birthdayNotReached ? age - 1 : age;
};

export function birthDateValidator(control: AbstractControl<string | null>): ValidationErrors | null {
  if (!control.value) return null;

  const birthDate = new Date(`${control.value}T00:00:00`);
  return ageInYears(birthDate) < MINIMUM_AGE ? { minor: true } : null;
}
