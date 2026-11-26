import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../ui/button/button-component';
import { LucideArrowRight } from '@lucide/angular';
import { ActionMenuComponent } from '../../ui/action-menu/action-menu.component';
import { ActionMenuItem } from '../../ui/action-menu/action-menu-item.model';

@Component({
  selector: 'app-generic-card-list-component',
  imports: [ButtonComponent, LucideArrowRight, ActionMenuComponent],
  standalone: true,
  templateUrl: './generic-card-list-component.html',
})
export class GenericCardListComponent {
  @Input() id!: number;
  @Input() title!: string;
  @Input() subtitle!: string;
  @Input() status!: boolean;

  @Output() edit = new EventEmitter<number>();
  @Output() deactivate = new EventEmitter<number>();
  @Output() showDetails = new EventEmitter<number>();
  @Output() activate = new EventEmitter<number>();

  getMenuItems(): ActionMenuItem[] {
    const items: ActionMenuItem[] = [
      {
        label: 'Editar',
        action: () => this.edit.emit(this.id),
      },
    ];

    if (this.status) {
      items.push({
        label: 'Desativar',
        danger: true,
        dividerAbove: true,
        action: () => this.deactivate.emit(this.id),
      });
    } else {
      items.push({
        label: 'Ativar',
        success: true,
        dividerAbove: true,
        action: () => this.activate.emit(this.id),
      });
    }

    return items;
  }

  onShowDetails(event: Event) {
    event.stopPropagation();
    this.showDetails.emit(this.id);
  }
}
