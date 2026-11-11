import { Component, Input } from '@angular/core';
import { CommonModule, CurrencyPipe, DecimalPipe } from '@angular/common';
import { LucideAlertTriangle, LucideTrendingUp } from '@lucide/angular';

/**
 * Dumb Component para exibição dos cards de indicadores-chave de performance (KPIs) de vendas:
 * - Ticket Médio por Transação
 * - Taxa de Risco de Vencimento do estoque em 90 dias
 */
@Component({
  selector: 'app-sales-kpi-cards',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DecimalPipe, LucideAlertTriangle, LucideTrendingUp],
  templateUrl: './sales-kpi-cards.component.html',
  styleUrls: ['./sales-kpi-cards.component.scss'],
})
export class SalesKpiCardsComponent {
  @Input() averageTicket: number = 0;
  @Input() expirationRiskRate: number = 0;
  @Input() loading: boolean = false;
}
