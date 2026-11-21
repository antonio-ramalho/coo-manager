import { AbstractControl, ValidationErrors } from '@angular/forms';


export function futureDateValidator(control: AbstractControl<string | null>): ValidationErrors | null {
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
