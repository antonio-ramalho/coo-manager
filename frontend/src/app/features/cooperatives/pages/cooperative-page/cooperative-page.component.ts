import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { CooperativeService } from '../../../../core/services/cooperative.service';
import { SubNavService } from '../../../../core/services/sub-nav.service';
import { SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';
import { ToastService } from '../../../../core/services/toast.service';
import { CooperativeResponse } from '../../../../core/models/cooperative.models';
import { Status } from '../../../../shared/enums/status';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { InputErrorComponent } from '../../../../shared/components/forms/input-error/input-error-component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

@Component({
  selector: 'app-cooperative-page',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonComponent,
    InputErrorComponent,
    LoadingSpinnerComponent,
  ],
  templateUrl: './cooperative-page.component.html',
  styleUrls: ['./cooperative-page.component.scss'],
})
export class CooperativePageComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly cooperativeService = inject(CooperativeService);
  private readonly subNavService = inject(SubNavService);
  private readonly toastService = inject(ToastService);
  private readonly destroyRef = inject(DestroyRef);

  readonly Status = Status;

  readonly tabs: SubNavTab[] = [
    { label: 'Cooperativa', route: '/home/cooperativa', exact: true },
  ];

  readonly cooperative = signal<CooperativeResponse | null>(null);
  readonly isLoading = signal<boolean>(true);
  readonly isEditing = signal<boolean>(false);
  readonly isSaving = signal<boolean>(false);

  cooperativeForm!: FormGroup;

  constructor() {
    this.subNavService.setTabs(this.tabs);
    this.destroyRef.onDestroy(() => {
      this.subNavService.clearTabs();
    });
  }

  ngOnInit(): void {
    this.initForm();
    this.loadCooperativeData();
  }

  private initForm(): void {
    this.cooperativeForm = this.fb.group({
      legalName: ['', [Validators.required]],
      tradeName: ['', [Validators.required]],
      cnpj: [{ value: '', disabled: true }],
      stateRegistration: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required]],
      foundationDate: [''],
      address: this.fb.group({
        street: ['', Validators.required],
        number: ['', Validators.required],
        neighborhood: ['', Validators.required],
        city: ['', Validators.required],
        zipCode: ['', Validators.required],
        state: ['', Validators.required],
      }),
    });
  }

  loadCooperativeData(): void {
    const tenantId = this.authService.tenantId();
    if (!tenantId) {
      this.isLoading.set(false);
      this.toastService.showWarning('Nenhuma cooperativa vinculada ao usuário logado.');
      return;
    }

    this.isLoading.set(true);
    this.cooperativeService.findById(tenantId).subscribe({
      next: (data) => {
        this.cooperative.set(data);
        this.populateForm(data);
        this.isLoading.set(false);
      },
      error: () => {
        this.isLoading.set(false);
        this.toastService.showError('Não foi possível carregar os dados da cooperativa.');
      },
    });
  }

  private populateForm(data: CooperativeResponse): void {
    this.cooperativeForm.patchValue({
      legalName: data.legalName,
      tradeName: data.tradeName,
      cnpj: data.cnpj,
      stateRegistration: data.stateRegistration,
      email: data.email,
      phone: data.phone,
      foundationDate: data.foundationDate,
      address: {
        street: data.address?.street,
        number: data.address?.number,
        neighborhood: data.address?.neighborhood,
        city: data.address?.city,
        zipCode: data.address?.zipCode,
        state: data.address?.state,
      },
    });
  }

  toggleEdit(): void {
    if (this.isEditing() && this.cooperative()) {
      this.populateForm(this.cooperative()!);
    }
    this.isEditing.update((v) => !v);
  }

  onSave(): void {
    if (this.cooperativeForm.invalid) {
      this.cooperativeForm.markAllAsTouched();
      return;
    }

    const current = this.cooperative();
    if (!current) return;

    this.isSaving.set(true);
    const formValue = this.cooperativeForm.getRawValue();

    this.cooperativeService.update(current.id, formValue).subscribe({
      next: (updated) => {
        this.cooperative.set(updated);
        this.isSaving.set(false);
        this.isEditing.set(false);
        this.toastService.showSuccess('Dados da cooperativa atualizados com sucesso!');
      },
      error: (err) => {
        this.isSaving.set(false);
        const msg = err.error?.message || 'Falha ao salvar alterações da cooperativa.';
        this.toastService.showError(msg);
      },
    });
  }
}

