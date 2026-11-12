import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAlertCircle,
  LucideAlertTriangle,
  LucideHelpCircle,
  LucideX,
} from '@lucide/angular';
import { ButtonComponent } from '../../ui/button/button-component';

export type ConfirmationDialogVariant = 'primary' | 'danger' | 'warning';

/**
 * Dumb Component reutilizável e global para confirmação de ações críticas do sistema
 * (ex: desativar cliente, cancelar venda, ativar lote, registrar estorno).
 */
@Component({
  selector: 'app-confirmation-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    LucideAlertTriangle,
    LucideAlertCircle,
    LucideHelpCircle,
    LucideX,
  ],
  templateUrl: './confirmation-dialog.component.html',
  styleUrls: ['./confirmation-dialog.component.scss'],
})
export class ConfirmationDialogComponent {
  @Input() isOpen = false;
  @Input() title = 'Confirmar ação';
  @Input() message = 'Tem certeza que deseja prosseguir com esta ação?';
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
  @Input() variant: ConfirmationDialogVariant = 'primary';
  @Input() isSubmitting = false;

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm(): void {
    if (!this.isSubmitting) {
      this.confirm.emit();
    }
  }

  onCancel(): void {
    if (!this.isSubmitting) {
      this.cancel.emit();
    }
  }
}
