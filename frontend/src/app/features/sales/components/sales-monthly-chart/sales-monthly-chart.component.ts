import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { MonthlySalesDto } from '../../models/sales-dashboard.model';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

/**
 * Dumb Component que renderiza o gráfico de Barras do Volume de Vendas Mensal
 * exibindo o faturamento consolidado por mês nos últimos meses.
 */
@Component({
  selector: 'app-sales-monthly-chart',
  standalone: true,
  imports: [CommonModule, BaseChartDirective, LoadingSpinnerComponent],
  templateUrl: './sales-monthly-chart.component.html',
  styleUrls: ['./sales-monthly-chart.component.scss'],
})
export class SalesMonthlyChartComponent implements OnChanges {
  @Input() monthlySales: MonthlySalesDto[] = [];
  @Input() loading = false;

  readonly chartType: 'bar' = 'bar';

  readonly chartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        titleFont: { family: "'Roboto', sans-serif" },
        bodyFont: { family: "'Roboto', sans-serif" },
        callbacks: {
          label: (context) => {
            const val = Number(context.raw ?? 0);
            const formatted = val.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
            const item = this.monthlySales[context.dataIndex];
            const countText = item ? ` (${item.salesCount} vendas)` : '';
            return ` Faturamento: ${formatted}${countText}`;
          },
        },
      },
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: { font: { family: "'Roboto', sans-serif", size: 12 }, color: '#6b7280' },
      },
      y: {
        grid: { color: '#f3f4f6' },
        ticks: {
          font: { family: "'Roboto', sans-serif", size: 11 },
          color: '#6b7280',
          callback: (value) => {
            const num = Number(value);
            return num >= 1000 ? `R$ ${(num / 1000).toFixed(0)}k` : `R$ ${num}`;
          },
        },
      },
    },
  };

  chartData: ChartData<'bar'> = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Faturamento',
        backgroundColor: '#16a34a',
        hoverBackgroundColor: '#15803d',
        borderRadius: 6,
        barPercentage: 0.55,
      },
    ],
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['monthlySales']) {
      this.updateChartData();
    }
  }

  private updateChartData(): void {
    const labels = this.monthlySales.map((m) => m.monthLabel);
    const data = this.monthlySales.map((m) => m.totalValue);

    this.chartData = {
      labels,
      datasets: [
        {
          data,
          label: 'Faturamento',
          backgroundColor: '#16a34a',
          hoverBackgroundColor: '#15803d',
          borderRadius: 6,
          barPercentage: 0.55,
        },
      ],
    };
  }
}
