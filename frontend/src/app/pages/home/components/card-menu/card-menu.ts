import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LucideHome, LucideTruck, LucideShoppingCart, LucideDollarSign, LucideUsers } from '@lucide/angular';

@Component({
  selector: 'app-card-menu',
  standalone: true,
  imports: [
    RouterModule,
    LucideHome, LucideTruck, LucideShoppingCart, LucideDollarSign, LucideUsers
  ],
  templateUrl: './card-menu.html',
  styleUrls: ['./card-menu.css']
})
export class CardMenuComponent {}
