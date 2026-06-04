import { Component } from '@angular/core';
import { LucideArrowDown } from '@lucide/angular';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [LucideArrowDown],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class DashboardComponent {}
