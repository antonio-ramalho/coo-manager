import { Component, Input } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlatpickrDirective } from 'angularx-flatpickr';
import { InputErrorComponent } from '../input-error/input-error-component';
import { InputProductSummary } from '../../../../features/inputs/models/input-product.model';

/**
 * Dumb Component responsável pelos campos do formulário de Lote (Estoque/Inventário).
 * Suporta modo avulso (com seleção de produto) ou modo vinculado ao cadastro inicial do produto.
 */
@Component({
  selector: 'app-batch-form',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    FlatpickrDirective,
    InputErrorComponent,
  ],
  templateUrl: './batch-form.component.html',
  styleUrl: './batch-form.component.scss',
})
export class BatchFormComponent {
  @Input({ required: true }) group!: FormGroup;
  @Input() showProductSelect: boolean = false;
  @Input() products: InputProductSummary[] = [];
}
