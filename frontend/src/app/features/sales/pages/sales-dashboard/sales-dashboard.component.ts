import { Component, inject } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, map, Observable, of, tap } from 'rxjs';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { SalesApiService } from '../../data/sales-api.service';
import { SalesDashboardDto } from '../../models/sales-dashboard.model';
import { SalesKpiCardsComponent } from '../../components/sales-kpi-cards/sales-kpi-cards.component';
import { SalesAbcChartComponent } from '../../components/sales-abc-chart/sales-abc-chart.component';
import { SalesMonthlyChartComponent } from '../../components/sales-monthly-chart/sales-monthly-chart.component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

/**
 * Smart Component para o painel de BI de Vendas e Curva ABC.
 * Consome o endpoint consolidado de BI do backend e injeta os dados reativamente
 * nos componentes visuais de KPIs e gráficos Chart.js / ng2-charts.
 */
@Component({
  selector: 'app-sales-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe,
    RouterLink,
    ButtonComponent,
    DesktopOnlyNoticeComponent,
    SalesKpiCardsComponent,
    SalesAbcChartComponent,
    SalesMonthlyChartComponent,
    LoadingSpinnerComponent,
  ],
  templateUrl: './sales-dashboard.component.html',
  styleUrl: './sales-dashboard.component.scss',
})
export class SalesDashboardComponent {
  private readonly salesApiService = inject(SalesApiService);
  private readonly breakpointObserver = inject(BreakpointObserver);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  loading = true;

  readonly dashboard$: Observable<SalesDashboardDto | null> = this.salesApiService.getSalesDashboard().pipe(
    tap(() => (this.loading = false)),
    catchError((err) => {
      console.error('Erro ao buscar métricas de BI de vendas:', err);
      this.loading = false;
      return of(null);
    }),
  );
}
