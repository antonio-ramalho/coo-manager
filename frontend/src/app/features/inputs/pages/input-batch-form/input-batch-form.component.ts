import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { PageTitleComponent } from '../../../../shared/components/layout/page-title/page-title.component';
import { StepperComponent } from '../../../../shared/components/layout/stepper/stepper.component';
import { BatchFormComponent } from '../../../../shared/components/forms/batch-form/batch-form.component';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { buildBatchForm } from '../../forms/batch-form.builder';
import { InputProductSummary } from '../../models/input-product.model';
import { InputBatch } from '../../models/input-batch.model';

/**
 * Smart Component para cadastro e edição avulsa de Lotes no estoque/inventário.
 * Reutiliza o StepperComponent e o BatchFormComponent dumb.
 */
@Component({
  selector: 'app-input-batch-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    PageTitleComponent,
    StepperComponent,
    BatchFormComponent,
  ],
  templateUrl: './input-batch-form.component.html',
  styleUrl: './input-batch-form.component.scss',
})
export class InputBatchFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly batchService = inject(InputBatchApiService);
  private readonly productService = inject(InputProductApiService);
  private readonly toastService = inject(ToastService);
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);

  readonly batchGroup: FormGroup = buildBatchForm(this.fb, true);
  readonly products = signal<InputProductSummary[]>([]);

  readonly stepLabels = ['Dados do Lote'];
  currentPage = 1;
  isSubmitting = false;
  isEditing = false;
  batchId: number | null = null;

  get isCurrentStepInvalid(): boolean {
    return this.batchGroup.invalid;
  }

  ngOnInit(): void {
    this.loadActiveProducts();
    this.checkIfIsEditing();
  }

  cancel(): void {
    this.location.back();
  }

  save(): void {
    if (this.batchGroup.invalid) {
      this.batchGroup.markAllAsTouched();
      this.toastService.showError('Preencha todos os campos obrigatórios corretamente.');
      return;
    }

    this.isSubmitting = true;
    const values = this.batchGroup.getRawValue();

    if (this.isEditing && this.batchId) {
      this.batchService
        .update(this.batchId, {
          expirationDate: values.expirationDate,
        })
        .subscribe({
          next: () => {
            this.toastService.showSuccess('Lote atualizado com sucesso!');
            this.location.back();
          },
          error: (err: unknown) => {
            this.isSubmitting = false;
            this.toastService.showError('Erro ao atualizar o lote.');
            console.error(err);
          },
        });
      return;
    }

    this.batchService
      .insert({
        batchCode: values.batchCode,
        inputProductId: values.inputProductId,
        quantityValue: Number(values.quantityValue),
        expirationDate: values.expirationDate,
      })
      .subscribe({
        next: () => {
          this.toastService.showSuccess('Lote cadastrado com sucesso!');
          this.location.back();
        },
        error: (err: unknown) => {
          this.isSubmitting = false;
          this.toastService.showError('Erro ao cadastrar lote.');
          console.error(err);
        },
      });
  }

  private loadActiveProducts(): void {
    this.productService.findAllByPage(0, 100).subscribe({
      next: (page) => {
        const active = page.content.filter(
          (p) => p.productCatalogStatus === 'ACTIVE' || !p.productCatalogStatus,
        );
        this.products.set(active);
      },
      error: (err) => {
        console.error('Erro ao carregar insumos para seleção:', err);
      },
    });
  }

  private checkIfIsEditing(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditing = true;
      this.batchId = Number(idParam);

      this.batchService.findById(this.batchId).subscribe({
        next: (batch: InputBatch) => {
          this.populateForm(batch);
        },
        error: () => {
          this.toastService.showError('Erro ao carregar dados do lote.');
        },
      });
    }
  }

  private populateForm(batch: InputBatch): void {
    this.batchGroup.patchValue({
      inputProductId: batch.inputProductId,
      batchCode: batch.batchCode,
      quantityValue: batch.currentQuantity,
      expirationDate: batch.expirationDate,
    });

    this.batchGroup.get('inputProductId')?.disable();
    this.batchGroup.get('batchCode')?.disable();
    this.batchGroup.get('quantityValue')?.disable();
  }
}
