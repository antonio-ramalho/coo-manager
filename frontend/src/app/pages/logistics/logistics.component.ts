import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-logistics',
  standalone: true,
  imports: [PageHeaderComponent],
  templateUrl: './logistics.component.html',
  styleUrl: './logistics.component.css',
})
export class LogisticsComponent {}
