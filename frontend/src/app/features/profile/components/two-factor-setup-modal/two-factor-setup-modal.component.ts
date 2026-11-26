import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import {
  LucideCheck,
  LucideCopy,
} from '@lucide/angular';
import { BaseModalComponent } from '../../../../shared/components/feedback/base-modal/base-modal.component';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { QrCodeGenerator } from '../../../../shared/utils/qr-code.util';

@Component({
  selector: 'app-two-factor-setup-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    BaseModalComponent,
    ButtonComponent,
    LucideCopy,
    LucideCheck,
  ],
  templateUrl: './two-factor-setup-modal.component.html',
  styleUrl: './two-factor-setup-modal.component.scss',
})
export class TwoFactorSetupModalComponent implements OnChanges {
  private readonly sanitizer = inject(DomSanitizer);

  @Input() isOpen = false;
  @Input() secret = '';
  @Input() qrCodeUri = '';
  @Input() loading = false;

  @Output() close = new EventEmitter<void>();
  @Output() confirm = new EventEmitter<string>();

  qrSvg: SafeHtml | null = null;
  copied = false;

  readonly codeControl = new FormControl('', [
    Validators.required,
    Validators.minLength(6),
    Validators.maxLength(6),
    Validators.pattern(/^\d{6}$/),
  ]);

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['qrCodeUri'] && this.qrCodeUri) {
      const rawSvg = QrCodeGenerator.generateSvg(this.qrCodeUri, 180);
      this.qrSvg = this.sanitizer.bypassSecurityTrustHtml(rawSvg);
    }
    if (changes['isOpen'] && this.isOpen) {
      this.codeControl.reset();
      this.copied = false;
    }
  }

  copySecret(): void {
    if (!this.secret) return;
    navigator.clipboard.writeText(this.secret).then(() => {
      this.copied = true;
      setTimeout(() => (this.copied = false), 2500);
    });
  }

  onSubmit(): void {
    if (this.codeControl.invalid || this.loading) {
      this.codeControl.markAsTouched();
      return;
    }
    this.confirm.emit(this.codeControl.value!);
  }

  onClose(): void {
    this.close.emit();
  }
}
