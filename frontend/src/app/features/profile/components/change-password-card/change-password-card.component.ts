import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  LucideCheck,
  LucideEye,
  LucideEyeOff,
  LucideKeyRound,
  LucideLock,
  LucideX,
} from '@lucide/angular';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { ChangePasswordRequest } from '../../../../core/models/change-password.model';
import { passwordMatchValidator } from '../../../../shared/validators/password-match.validator';
import {
  PasswordStrengthResult,
  evaluatePasswordStrength,
} from '../../../../shared/utils/password-strength.util';

@Component({
  selector: 'app-change-password-card',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    LucideKeyRound,
    LucideLock,
    LucideEye,
    LucideEyeOff,
    LucideCheck,
    LucideX,
    ButtonComponent,
  ],
  templateUrl: './change-password-card.component.html',
  styleUrl: './change-password-card.component.scss',
})
export class ChangePasswordCardComponent {
  private readonly fb = inject(FormBuilder);

  @Input() loading = false;
  @Output() passwordChange = new EventEmitter<ChangePasswordRequest>();

  showCurrent = false;
  showNew = false;
  showConfirm = false;

  readonly form: FormGroup = this.fb.group(
    {
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
    },
    { validators: [passwordMatchValidator] },
  );

  get strength(): PasswordStrengthResult {
    const val = this.form.get('newPassword')?.value || '';
    return evaluatePasswordStrength(val);
  }

  get isMismatch(): boolean {
    const confirm = this.form.get('confirmPassword');
    return (
      !!this.form.errors?.['passwordMismatch'] &&
      !!confirm?.touched &&
      !!confirm?.value
    );
  }

  toggleCurrent(): void {
    this.showCurrent = !this.showCurrent;
  }

  toggleNew(): void {
    this.showNew = !this.showNew;
  }

  toggleConfirm(): void {
    this.showConfirm = !this.showConfirm;
  }

  onSubmit(): void {
    if (this.form.invalid || this.loading) {
      this.form.markAllAsTouched();
      return;
    }

    const val = this.form.value;
    this.passwordChange.emit({
      currentPassword: val.currentPassword,
      newPassword: val.newPassword,
      confirmPassword: val.confirmPassword,
    });
  }

  resetForm(): void {
    this.form.reset();
  }
}
