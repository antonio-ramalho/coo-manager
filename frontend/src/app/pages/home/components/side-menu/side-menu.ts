import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import {
  LucideHouse,
  LucideTruck,
  LucideShoppingCart,
  LucideDollarSign,
  LucideUsers,
  LucideChevronLeft,
  LucideChevronRight,
} from '@lucide/angular';

@Component({
  selector: 'app-side-menu',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive,
    LucideHouse,
    LucideTruck,
    LucideShoppingCart,
    LucideDollarSign,
    LucideUsers,
    LucideChevronLeft,
    LucideChevronRight,
  ],
  templateUrl: './side-menu.html',
  styleUrl: './side-menu.css',
})
export class SideMenuComponent {
  isExpanded = true;

  toggleMenu() {
    this.isExpanded = !this.isExpanded;
  }
}
