import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-financial',
  standalone: true,
  imports: [PageHeaderComponent],
  templateUrl: './financial.component.html',
  styleUrl: './financial.component.css',
})
export class FinancialComponent {}
