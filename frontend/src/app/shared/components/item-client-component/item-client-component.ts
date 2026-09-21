import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CpfCnpjPipe } from '../../pipes/cpf-cnpj-pipe';
import { ButtonComponent } from '../button/button';
import { LucideArrowRight, LucideEllipsis } from '@lucide/angular';
import { OverlayModule } from '@angular/cdk/overlay';

@Component({
  selector: 'app-item-client-component',
  imports: [CpfCnpjPipe, ButtonComponent, LucideArrowRight, LucideEllipsis, OverlayModule],
  standalone: true,
  templateUrl: './item-client-component.html',
})
export class ItemClientComponent {
  @Input() id!: string | number;
  @Input() name!: string;
  @Input() documentNumber!: string;

  @Output() edit = new EventEmitter<string | number>();
  @Output() deactivate = new EventEmitter<string | number>();
  @Output() showDetails = new EventEmitter<string | number>();

  isMenuOpen: boolean = false;

  toggleMenu(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = !this.isMenuOpen;
  }

  onEdit(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = false;
    this.edit.emit(this.id);
  }

  onDeactivate(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = false;
    this.deactivate.emit(this.id);
  }

  onShowDetails(event: Event) {
    event.stopPropagation();
    this.showDetails.emit(this.id);
  }
}
