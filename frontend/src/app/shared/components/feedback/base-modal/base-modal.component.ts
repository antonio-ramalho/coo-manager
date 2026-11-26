import {
  Component,
  EventEmitter,
  HostListener,
  Input,
  Output,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideX } from '@lucide/angular';

export type ModalSize = 'sm' | 'md' | 'lg' | 'xl' | 'full';

/**
 * Dumb Component base para modais e diálogos da aplicação.
 * Padroniza o backdrop, animações, acessibilidade, tamanhos e slots de cabeçalho, corpo e rodapé.
 */
@Component({
  selector: 'app-base-modal',
  standalone: true,
  imports: [CommonModule, LucideX],
  templateUrl: './base-modal.component.html',
  styleUrls: ['./base-modal.component.scss'],
})
export class BaseModalComponent {
  @Input() isOpen = false;
  @Input() title = '';
  @Input() subtitle?: string;
  @Input() size: ModalSize = 'md';
  @Input() minHeight?: string;
  @Input() height?: string;
  @Input() icon?: any;
  @Input() noPadding = false;
  @Input() closeOnBackdrop = true;
  @Input() closeOnEscape = true;
  @Input() showCloseButton = true;
  @Input() ariaLabel?: string;

  @Output() close = new EventEmitter<void>();

  @HostListener('document:keydown.escape', ['$event'])
  onEscapeKey(event?: Event): void {
    if (this.isOpen && this.closeOnEscape) {
      event?.preventDefault();
      this.onClose();
    }
  }

  onBackdropClick(): void {
    if (this.closeOnBackdrop) {
      this.onClose();
    }
  }

  onClose(): void {
    this.close.emit();
  }

  getSizeClass(): string {
    switch (this.size) {
      case 'sm':
        return 'modal-sm';
      case 'lg':
        return 'modal-lg';
      case 'xl':
        return 'modal-xl';
      case 'full':
        return 'modal-full';
      case 'md':
      default:
        return 'modal-md';
    }
  }
}
