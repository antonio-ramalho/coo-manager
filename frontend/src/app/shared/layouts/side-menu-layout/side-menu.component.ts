import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  LucideLayoutDashboard,
  LucideTruck,
  LucideShoppingCart,
  LucideDollarSign,
  LucideUsers,
  LucideChevronLeft,
  LucideChevronRight, LucideHouse,
} from '@lucide/angular';

@Component({
  selector: 'app-side-menu',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    LucideLayoutDashboard,
    LucideTruck,
    LucideShoppingCart,
    LucideDollarSign,
    LucideUsers,
    LucideChevronLeft,
    LucideChevronRight,
  ],
  templateUrl: './side-menu.component.html',
  styleUrl: './side-menu.component.scss',
})
export class SideMenuComponent {
  isExpanded = true;

  toggleMenu() {
    this.isExpanded = !this.isExpanded;
  }
}
