import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../ui/button/button-component';
import { LucideArrowRight, LucideEllipsis } from '@lucide/angular';
import { OverlayModule } from '@angular/cdk/overlay';

@Component({
  selector: 'app-generic-card-list-component',
  imports: [ButtonComponent, LucideArrowRight, LucideEllipsis, OverlayModule],
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

  onActivate(event: Event) {
    event.stopPropagation();
    this.isMenuOpen = false;
    this.activate.emit(this.id);
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
