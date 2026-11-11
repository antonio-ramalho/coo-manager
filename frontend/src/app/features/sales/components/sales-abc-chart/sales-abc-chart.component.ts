import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { TopSellingProductDto } from '../../models/sales-dashboard.model';
import { MeasureUnitPipe } from '../../../../shared/pipes/measure-unit-pipe';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

/**
 * Dumb Component que renderiza o gráfico de Rosca (Doughnut) da Curva ABC
 * exibindo os insumos com maior giro e volume de vendas.
 */
@Component({
  selector: 'app-sales-abc-chart',
  standalone: true,
  imports: [CommonModule, BaseChartDirective, LoadingSpinnerComponent],
  templateUrl: './sales-abc-chart.component.html',
  styleUrls: ['./sales-abc-chart.component.scss'],
})
export class SalesAbcChartComponent implements OnChanges {
  private readonly measureUnitPipe = new MeasureUnitPipe();

  @Input() products: TopSellingProductDto[] = [];
  @Input() loading = false;

  readonly chartType: 'doughnut' = 'doughnut';

  readonly chartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          boxWidth: 12,
          padding: 14,
          font: { family: "'Roboto', sans-serif", size: 12 },
          color: '#374151',
        },
      },
      tooltip: {
        titleFont: { family: "'Roboto', sans-serif" },
        bodyFont: { family: "'Roboto', sans-serif" },
        callbacks: {
          label: (context) => {
            const index = context.dataIndex;
            const item = this.products[index];
            const translatedUnit = item?.measureUnit
              ? this.measureUnitPipe.transform(item.measureUnit)
              : '';
            const unitSuffix = translatedUnit ? ` ${translatedUnit}` : '';
            return ` ${context.label}: ${context.raw}${unitSuffix}`;
          },
        },
      },
    },
    cutout: '65%',
  };

  chartData: ChartData<'doughnut'> = {
    labels: [],
    datasets: [
      {
        data: [],
        backgroundColor: ['#16a34a', '#0891b2', '#f59e0b', '#8b5cf6', '#ec4899', '#64748b'],
        borderWidth: 2,
        borderColor: '#ffffff',
        hoverOffset: 4,
      },
    ],
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['products']) {
      this.updateChartData();
    }
  }

  private updateChartData(): void {
    const labels = this.products.map((p) => p.productName);
    const data = this.products.map((p) => p.totalQuantitySold);

    this.chartData = {
      labels,
      datasets: [
        {
          data,
          backgroundColor: ['#16a34a', '#0891b2', '#f59e0b', '#8b5cf6', '#ec4899', '#64748b'],
          borderWidth: 2,
          borderColor: '#ffffff',
          hoverOffset: 4,
        },
      ],
    };
  }
}
