import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { PageTitleComponent } from '../../../../shared/components/layout/page-title/page-title.component';
import { StepperComponent } from '../../../../shared/components/layout/stepper/stepper.component';
import { ProductFormComponent } from '../../../../shared/components/forms/product-form/product-form.component';
import { BatchFormComponent } from '../../../../shared/components/forms/batch-form/batch-form.component';
import { InputProductApiService } from '../../data/input-product-api.service';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { buildProductForm } from '../../forms/product-form.builder';
import { buildBatchForm } from '../../forms/batch-form.builder';
import { InputProduct } from '../../models/input-product.model';

/**
 * Smart Component para cadastro e edição de insumos no catálogo.
 * Suporta o fluxo de Stepper e o cadastro opcional de lote inicial de estoque.
 */
@Component({
  selector: 'app-input-product-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    PageTitleComponent,
    StepperComponent,
    ProductFormComponent,
    BatchFormComponent,
  ],
  templateUrl: './input-product-form.component.html',
  styleUrl: './input-product-form.component.scss',
})
export class InputProductFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(InputProductApiService);
  private readonly batchService = inject(InputBatchApiService);
  private readonly toastService = inject(ToastService);
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);

  readonly productGroup: FormGroup = buildProductForm(this.fb);
  readonly batchGroup: FormGroup = buildBatchForm(this.fb, false);

  readonly includeInitialBatch = signal<boolean>(false);
  currentPage = 1;
  isSubmitting = false;
  isEditing = false;
  productId: number | null = null;

  get stepLabels(): string[] {
    if (this.isEditing || !this.includeInitialBatch()) {
      return ['Dados do Insumo'];
    }
    return ['Dados do Insumo', 'Lote Inicial'];
  }

  get totalSteps(): number {
    return this.stepLabels.length;
  }

  get isCurrentStepInvalid(): boolean {
    if (this.currentPage === 1) {
      return this.productGroup.invalid;
    }
    return this.batchGroup.invalid;
  }

  get isSubmitDisabled(): boolean {
    if (this.isEditing) {
      return this.productGroup.invalid;
    }
    if (this.includeInitialBatch()) {
      return this.productGroup.invalid || this.batchGroup.invalid;
    }
    return this.productGroup.invalid;
  }

  ngOnInit(): void {
    this.checkIfIsEditing();
  }

  setInitialBatch(value: boolean): void {
    this.includeInitialBatch.set(value);
    if (!value && this.currentPage > 1) {
      this.currentPage = 1;
    }
  }

  toggleInitialBatch(event: Event): void {
    const isChecked = (event.target as HTMLInputElement).checked;
    this.setInitialBatch(isChecked);
  }

  nextPage(): void {
    if (this.currentPage < this.totalSteps) {
      this.currentPage++;
    }
  }

  backPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  cancel(): void {
    this.location.back();
  }

  save(): void {
    if (this.isCurrentStepInvalid) {
      this.markAllTouched();
      this.toastService.showError('Preencha todos os campos obrigatórios corretamente.');
      return;
    }

    if (this.includeInitialBatch() && this.batchGroup.invalid) {
      this.batchGroup.markAllAsTouched();
      this.toastService.showError('Preencha os dados do lote inicial corretamente.');
      return;
    }

    this.isSubmitting = true;
    const productValues = this.productGroup.getRawValue();

    if (this.isEditing && this.productId) {
      this.productService
        .update(this.productId, {
          productName: productValues.productName,
          priceValue: Number(productValues.price),
        })
        .subscribe({
          next: () => {
            this.toastService.showSuccess('Insumo atualizado com sucesso!');
            this.location.back();
          },
          error: (err: unknown) => {
            this.isSubmitting = false;
            this.toastService.showError('Erro ao atualizar o insumo.');
            console.error(err);
          },
        });
      return;
    }

    const insertProductDto = {
      productName: productValues.productName,
      productCode: productValues.productCode,
      measureUnit: productValues.measureUnit,
      ncmValue: productValues.ncm,
      priceValue: Number(productValues.price),
    };

    if (this.includeInitialBatch()) {
      const batchValues = this.batchGroup.getRawValue();

      this.productService
        .insert(insertProductDto)
        .pipe(
          switchMap((createdProduct) => {
            return this.batchService.insert({
              batchCode: batchValues.batchCode,
              inputProductId: createdProduct.id,
              quantityValue: Number(batchValues.quantityValue),
              expirationDate: batchValues.expirationDate,
            });
          }),
        )
        .subscribe({
          next: () => {
            this.toastService.showSuccess('Insumo e lote inicial cadastrados com sucesso!');
            this.location.back();
          },
          error: (err: unknown) => {
            this.isSubmitting = false;
            this.toastService.showError('Erro ao cadastrar insumo e lote inicial.');
            console.error(err);
          },
        });
    } else {
      this.productService.insert(insertProductDto).subscribe({
        next: () => {
          this.toastService.showSuccess('Insumo cadastrado com sucesso!');
          this.location.back();
        },
        error: (err: unknown) => {
          this.isSubmitting = false;
          this.toastService.showError('Erro ao cadastrar insumo.');
          console.error(err);
        },
      });
    }
  }

  private markAllTouched(): void {
    this.productGroup.markAllAsTouched();
    if (this.includeInitialBatch()) {
      this.batchGroup.markAllAsTouched();
    }
  }

  private checkIfIsEditing(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditing = true;
      this.productId = Number(idParam);

      this.productService.findById(this.productId).subscribe({
        next: (product: InputProduct) => {
          this.populateForm(product);
        },
        error: () => {
          this.toastService.showError('Erro ao carregar dados do insumo.');
        },
      });
    }
  }

  private populateForm(product: InputProduct): void {
    this.productGroup.patchValue({
      productName: product.productName,
      productCode: product.productCode,
      measureUnit: product.measureUnit,
      price: product.price?.value ?? null,
      ncm: product.ncm,
    });

    this.productGroup.get('productCode')?.disable();
    this.productGroup.get('measureUnit')?.disable();
    this.productGroup.get('ncm')?.disable();
  }
}
