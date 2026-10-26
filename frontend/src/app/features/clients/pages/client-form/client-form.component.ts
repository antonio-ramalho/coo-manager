import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { PageTitleComponent } from '../../../../shared/components/layout/page-title/page-title.component';
import { StepperComponent } from '../../../../shared/components/layout/stepper/stepper.component';
import { PersonForm } from '../../../../shared/components/forms/person-form/person-form-component';
import { NaturalPersonFormComponent } from '../../../../shared/components/forms/natural-person-form/natural-person-form-component';
import { JuridicPersonFormComponent } from '../../../../shared/components/forms/juridic-person-form/juridic-person-form-component';
import { AddressFormComponent } from '../../../../shared/components/forms/address-form/address-form-component';

import { PersonType } from '../../../../shared/enums/person-type';
import { ClientApiService } from '../../data/client-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { buildClientForm } from '../../forms/client-form.builder';
import { applyPersonTypeValidators } from '../../forms/person-type-rules';
import { Client, ClientPayload } from '../../models/client.model';

/**
 * Smart Component para cadastro e edição de clientes (Pessoa Física e Pessoa Jurídica).
 * Centraliza o fluxo de stepper e validações dinâmicas por estratégia.
 */
@Component({
  selector: 'app-client-form',
  imports: [
    ReactiveFormsModule,
    PageTitleComponent,
    StepperComponent,
    PersonForm,
    NaturalPersonFormComponent,
    JuridicPersonFormComponent,
    AddressFormComponent,
  ],
  templateUrl: './client-form.component.html',
})
export class ClientFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly clientService = inject(ClientApiService);
  private readonly toastService = inject(ToastService);
  private readonly location = inject(Location);
  private readonly route = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);

  readonly PersonType = PersonType;
  readonly masterForm: FormGroup = buildClientForm(this.fb);

  currentPage = 1;
  readonly totalSteps = 3;
  readonly stepLabels = ['Informações Básicas', 'Dados Específicos', 'Localização'];

  isSubmitting = false;
  isEditing = false;
  clientId: number | null = null;

  get basicInfoGroup(): FormGroup {
    return this.masterForm.get('basicInfo') as FormGroup;
  }

  get addressGroup(): FormGroup {
    return this.masterForm.get('address') as FormGroup;
  }

  get specificDataGroup(): FormGroup {
    return this.masterForm.get('specificData') as FormGroup;
  }

  get personType(): PersonType {
    return this.basicInfoGroup.get('personType')?.value;
  }

  get isCurrentStepInvalid(): boolean {
    if (this.currentPage === 1) return this.basicInfoGroup.invalid;
    if (this.currentPage === 2) return this.specificDataGroup.invalid;
    return this.addressGroup.invalid;
  }

  ngOnInit(): void {
    this.setupDynamicValidations();
    this.checkIfIsEditing();
  }

  private setupDynamicValidations(): void {
    applyPersonTypeValidators(this.specificDataGroup, PersonType.NATURAL);

    this.basicInfoGroup
      .get('personType')
      ?.valueChanges.pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((type: PersonType) => {
        this.specificDataGroup.reset();
        applyPersonTypeValidators(this.specificDataGroup, type);
      });
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
    if (this.masterForm.invalid) {
      this.masterForm.markAllAsTouched();
      this.toastService.showError('Preencha todos os campos obrigatórios corretamente.');
      return;
    }

    this.isSubmitting = true;
    const rawValues = this.masterForm.getRawValue();

    const payload: ClientPayload = {
      ...rawValues.basicInfo,
      address: rawValues.address,
      ...rawValues.specificData,
    };

    const request$ = this.isEditing && this.clientId
      ? this.clientService.update(this.clientId, payload)
      : this.clientService.insert(payload);

    const successMessage = this.isEditing
      ? 'Cliente atualizado com sucesso!'
      : 'Cliente cadastrado com sucesso!';

    request$.subscribe({
      next: () => {
        this.toastService.showSuccess(successMessage);
        this.location.back();
      },
      error: () => {
        this.isSubmitting = false;
      },
    });
  }

  private checkIfIsEditing(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditing = true;
      this.clientId = Number(idParam);

      this.clientService.findById(this.clientId).subscribe({
        next: (client: Client) => {
          this.populateForm(client);
        },
        error: () => {
          this.toastService.showError('Erro ao carregar dados do cliente.');
        },
      });
    }
  }

  private populateForm(client: Client): void {
    const isNatural = client.personType === PersonType.NATURAL;

    this.masterForm.patchValue({
      basicInfo: {
        legalName: client.legalName,
        email: client.email,
        phone: client.phone,
        personType: client.personType,
      },
      address: client.address,
      specificData: {
        cpf: isNatural ? client.cpf : null,
        cnpj: isNatural ? null : client.cnpj,
        birthDate: isNatural ? client.birthDate : null,
        foundationDate: isNatural ? null : client.foundationDate,
        gender: isNatural ? client.gender : null,
        tradeName: isNatural ? null : client.tradeName,
      },
    });

    this.masterForm.get('basicInfo.personType')?.disable();
    this.masterForm.get('specificData.cpf')?.disable();
    this.masterForm.get('specificData.cnpj')?.disable();
  }
}
