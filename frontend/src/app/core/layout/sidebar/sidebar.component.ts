import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  LucideBarcode,
  LucideChevronLeft,
  LucideChevronRight,
  LucideDollarSign,
  LucideLayoutDashboard,
  LucideShoppingCart,
  LucideTruck,
  LucideUsers,
  LucideX,
} from '@lucide/angular';
import { NavSection } from '../../models/nav-item.model';

/**
 * Dumb Component responsável pela renderização da barra lateral de navegação.
 * Controlado inteiramente pelo AdminLayout via Inputs e Outputs.
 */
@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink,
    RouterLinkActive,
    LucideChevronLeft,
    LucideChevronRight,
    LucideLayoutDashboard,
    LucideTruck,
    LucideDollarSign,
    LucideUsers,
    LucideShoppingCart,
    LucideBarcode,
    LucideX,
  ],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
})
export class SidebarComponent {
  @Input({ required: true }) sections: NavSection[] = [];
  @Input() collapsed = false;
  @Input() isMobile = false;

  @Output() toggleCollapse = new EventEmitter<void>();
  @Output() closeMobile = new EventEmitter<void>();
  @Output() navigate = new EventEmitter<void>();

  onNavigate(): void {
    this.navigate.emit();
  }
}
