import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-sales',
  standalone: true,
  imports: [PageHeaderComponent],
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.css',
})
export class SalesComponent {}
