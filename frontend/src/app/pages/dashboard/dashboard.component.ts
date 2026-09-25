import { HeaderLayoutComponent } from '../../shared/layouts/header-layout/header-layout.component';
import { Component } from '@angular/core';
import { LucideArrowDown } from '@lucide/angular';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [LucideArrowDown, HeaderLayoutComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {}
