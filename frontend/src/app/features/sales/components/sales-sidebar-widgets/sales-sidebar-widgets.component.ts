import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RecentSaleItem } from '../../models/sales-transaction.model';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

/**
 * Dumb Component que renderiza o widget lateral com as últimas 5 vendas efetuadas.
 */
@Component({
  selector: 'app-sales-sidebar-widgets',
  standalone: true,
  imports: [CommonModule, DatePipe, LoadingSpinnerComponent],
  templateUrl: './sales-sidebar-widgets.component.html',
  styleUrls: ['./sales-sidebar-widgets.component.scss'],
})
export class SalesSidebarWidgetsComponent {
  @Input() recentSales: RecentSaleItem[] = [];
  @Input() loadingRecent: boolean = false;
}
