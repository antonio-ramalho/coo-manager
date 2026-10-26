import { Component, Input } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlatpickrDirective } from 'angularx-flatpickr';
import { Observable } from 'rxjs';
import { InputErrorComponent } from '../input-error/input-error-component';
import { SearchableSelectComponent } from '../searchable-select/searchable-select.component';
import { InputProductSummary } from '../../../../features/inputs/models/input-product.model';

/**
 * Dumb Component responsável pelos campos do formulário de Lote (Estoque/Inventário).
 * Suporta modo avulso com seleção pesquisável de produto via SearchableSelectComponent
 * ou modo vinculado ao cadastro inicial do produto.
 */
@Component({
  selector: 'app-batch-form',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    FlatpickrDirective,
    InputErrorComponent,
    SearchableSelectComponent,
  ],
  templateUrl: './batch-form.component.html',
  styleUrl: './batch-form.component.scss',
})
export class BatchFormComponent {
  @Input({ required: true }) group!: FormGroup;
  @Input() showProductSelect: boolean = false;
  @Input() products: InputProductSummary[] = [];
  @Input() searchProductsFn?: (term: string) => Observable<InputProductSummary[]>;

  readonly productLabelFormatter = (product: InputProductSummary): string => {
    return `${product.productName} (${product.productCode})`;
  };

  readonly productSearchFunction = (product: InputProductSummary, term: string): boolean => {
    const cleanTerm = term.toLowerCase();
    const nameMatch = product.productName?.toLowerCase().includes(cleanTerm);
    const codeMatch = product.productCode?.toLowerCase().includes(cleanTerm);
    return Boolean(nameMatch || codeMatch);
  };
}
