import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, DatePipe } from '@angular/common';
import { Observable } from 'rxjs';
import { LucideCalendar, LucideFileText, LucideUser, LucideUserCheck } from '@lucide/angular';
import { SearchableSelectComponent } from '../../../../shared/components/forms/searchable-select/searchable-select.component';
import { InputErrorComponent } from '../../../../shared/components/forms/input-error/input-error-component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';
import { ClientSummary } from '../../../clients/models/client.model';
import { formatDocument } from '../../../../shared/utils/document-format';
import { SalesForm } from '../../forms/sales-form.builder';
import { SaleClientPreview } from '../../models/sales-transaction.model';

/**
 * Dumb Component para o Passo 1: Busca e seleção do Cliente/Cooperado.
 * Renderiza apenas os dados essenciais para conferência: Nome, CPF, Data de Nascimento e Situação.
 */
@Component({
  selector: 'app-sale-client-step',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    ReactiveFormsModule,
    SearchableSelectComponent,
    InputErrorComponent,
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
  @Input({ required: true }) formGroup!: FormGroup<SalesForm>;
  @Input() clientPreview: SaleClientPreview | null = null;
  @Input() isLoadingDetails = false;
  @Input({ required: true }) searchClientsFn!: (term: string) => Observable<ClientSummary[]>;

  @Output() clientSelected = new EventEmitter<ClientSummary | null>();
  @Output() proceed = new EventEmitter<void>();

  clientLabelFormatter = (client: ClientSummary): string => {
    return `${client.legalName} (${formatDocument(client.documentNumber)})`;
  };

  formatDoc(doc: string): string {
    return formatDocument(doc);
  }

  onOptionSelected(item: ClientSummary | null): void {
    this.clientSelected.emit(item);
  }
}
