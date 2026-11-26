import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { LucideAlertTriangle } from '@lucide/angular';
import { BaseModalComponent } from '../../../../shared/components/feedback/base-modal/base-modal.component';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';

@Component({
  selector: 'app-two-factor-disable-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    BaseModalComponent,
    ButtonComponent,
    LucideAlertTriangle,
  ],
  templateUrl: './two-factor-disable-modal.component.html',
  styleUrl: './two-factor-disable-modal.component.scss',
})
export class TwoFactorDisableModalComponent implements OnChanges {
  @Input() isOpen = false;
  @Input() loading = false;

  @Output() close = new EventEmitter<void>();
  @Output() disable = new EventEmitter<string>();

  readonly codeControl = new FormControl('', [
    Validators.required,
    Validators.minLength(6),
    Validators.maxLength(6),
    Validators.pattern(/^\d{6}$/),
  ]);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isOpen'] && this.isOpen) {
      this.codeControl.reset();
    }
  }

  onSubmit(): void {
    if (this.codeControl.invalid || this.loading) {
      this.codeControl.markAsTouched();
      return;
    }
    this.disable.emit(this.codeControl.value!);
  }

  onClose(): void {
    this.close.emit();
  }
}
