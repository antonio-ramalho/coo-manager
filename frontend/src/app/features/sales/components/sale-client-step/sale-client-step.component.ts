import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { LucideCalendar, LucideFileText, LucideUser, LucideUserCheck } from '@lucide/angular';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';
import { formatDocument } from '../../../../shared/utils/document-format';
import { SaleClientPreview } from '../../models/sales-transaction.model';

/**
 * Dumb Component responsável pela visão dos dados cadastrais do cliente selecionado na venda.
 * Exibe spinner durante a busca ou estado vazio orientativo antes da seleção.
 */
@Component({
  selector: 'app-sale-client-step',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    LoadingSpinnerComponent,
    LucideUserCheck,
    LucideUser,
    LucideFileText,
    LucideCalendar,
  ],
  templateUrl: './sale-client-step.component.html',
  styleUrl: './sale-client-step.component.scss',
})
export class SaleClientStepComponent {
  @Input() clientPreview: SaleClientPreview | null = null;
  @Input() isLoadingDetails = false;

  formatDoc(doc: string): string {
    return formatDocument(doc);
  }
}
