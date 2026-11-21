import {
  Component,
  computed,
  EventEmitter,
  inject,
  Input,
  Output,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectedPosition, OverlayModule } from '@angular/cdk/overlay';
import { LucideEllipsis } from '@lucide/angular';
import { ActionMenuItem } from './action-menu-item.model';
import { AuthService } from '../../../../core/services/auth.service';

/**
 * Dumb Component reutilizável para menus de contexto/dropdown acionados por botão.
 * Utiliza Angular CDK Overlay garantindo posicionamento dinâmico, acessibilidade e fechamento seguro.
 */
@Component({
  selector: 'app-action-menu',
  standalone: true,
  imports: [CommonModule, OverlayModule, LucideEllipsis],
  templateUrl: './action-menu.component.html',
  styleUrls: ['./action-menu.component.scss'],
})
export class ActionMenuComponent {
  private readonly authService = inject(AuthService);

  private readonly _items = signal<ActionMenuItem[]>([]);

  @Input()
  set items(value: ActionMenuItem[] | null | undefined) {
    this._items.set(value || []);
  }
  get items(): ActionMenuItem[] {
    return this._items();
  }

  @Input() dataContext?: any;
  @Input() triggerTitle = 'Mais opções';
  @Input() triggerAriaLabel = 'Abrir menu de ações';
  @Input() disabled = false;
  @Input() minWidth = '160px';
  @Input() placement: 'bottom-right' | 'bottom-left' | 'top-right' | 'top-left' = 'bottom-right';

  @Output() menuOpened = new EventEmitter<void>();
  @Output() menuClosed = new EventEmitter<void>();

  readonly isOpen = signal(false);

  readonly visibleItems = computed(() => {
    return this._items().filter((item) => {
      if (!item.permission) return true;
      return this.authService.hasPermission(item.permission);
    });
  });

  get overlayPositions(): ConnectedPosition[] {
    switch (this.placement) {
      case 'bottom-left':
        return [
          { originX: 'start', originY: 'bottom', overlayX: 'start', overlayY: 'top', offsetY: 6 },
          { originX: 'start', originY: 'top', overlayX: 'start', overlayY: 'bottom', offsetY: -6 },
        ];
      case 'top-right':
        return [
          { originX: 'end', originY: 'top', overlayX: 'end', overlayY: 'bottom', offsetY: -6 },
          { originX: 'end', originY: 'bottom', overlayX: 'end', overlayY: 'top', offsetY: 6 },
        ];
      case 'top-left':
        return [
          { originX: 'start', originY: 'top', overlayX: 'start', overlayY: 'bottom', offsetY: -6 },
          { originX: 'start', originY: 'bottom', overlayX: 'start', overlayY: 'top', offsetY: 6 },
        ];
      case 'bottom-right':
      default:
        return [
          { originX: 'end', originY: 'bottom', overlayX: 'end', overlayY: 'top', offsetY: 6 },
          { originX: 'end', originY: 'top', overlayX: 'end', overlayY: 'bottom', offsetY: -6 },
        ];
    }
  }

  toggle(event?: Event): void {
    if (this.disabled) return;
    if (event) {
      event.stopPropagation();
    }
    if (this.isOpen()) {
      this.close();
    } else {
      this.open();
    }
  }

  open(): void {
    if (this.disabled || this.isOpen()) return;
    this.isOpen.set(true);
    this.menuOpened.emit();
  }

  close(): void {
    if (!this.isOpen()) return;
    this.isOpen.set(false);
    this.menuClosed.emit();
  }

  onItemClick(item: ActionMenuItem, event: Event): void {
    event.stopPropagation();
    if (item.disabled) return;

    this.close();
    if (item.action) {
      item.action(this.dataContext);
    }
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Escape' && this.isOpen()) {
      this.close();
      event.stopPropagation();
    }
  }
}
