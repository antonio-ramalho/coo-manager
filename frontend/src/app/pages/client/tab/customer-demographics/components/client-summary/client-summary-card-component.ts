import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideCircleAlert } from '@lucide/angular';

@Component({
  selector: 'app-client-summary',
  standalone: true,
  imports: [CommonModule, LucideCircleAlert],
  templateUrl: './client-summary-card-component.html',
})
export class ClientSummaryComponent {
  @Input() titulo: string = 'Total de clientes';
  @Input() valor: string | number = '101';
  @Input() subtexto: string = '+2 no último mês';
}
