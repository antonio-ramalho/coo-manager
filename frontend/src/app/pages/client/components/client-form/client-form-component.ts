import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AddressFormComponent } from '../../../../shared/components/forms/address-form/address-form-component';
import {
  JuridicPersonFormComponent
} from '../../../../shared/components/forms/juridic-person-form/juridic-person-form-component';
import {
  NaturalPersonFormComponent
} from '../../../../shared/components/forms/natural-person-form/natural-person-form-component';
import { CustomValidators } from '../../../../core/validators/CustomValidators';
import { StepperLayoutComponent } from '../../../../shared/layouts/stepper-layout/stepper-layout-component';
import { PersonForm } from '../../../../shared/components/forms/person-form/person-form-component';
import { ClientApiService } from '../../../../core/api/client-api';
import { ToastService } from '../../../../core/services/toast-service';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { IJuridicClientDto, INaturalClientDto } from '../../../../core/interfaces/IClient';

@Component({
  selector: 'app-client-form-component',
  imports: [
    AddressFormComponent,
    JuridicPersonFormComponent,
    NaturalPersonFormComponent,
    ReactiveFormsModule,
    StepperLayoutComponent,
    PersonForm,
  ],
  templateUrl: '/client-form-component.html',
})
export class ClientFormComponent implements OnInit {
  masterForm: FormGroup;
  currentPage: number = 1;
  pages: number = 3;
  isSubmitting: boolean = false;
  isEditing: boolean = false;
  clientId: number | null = null;

  clientService = inject(ClientApiService);
  toastService = inject(ToastService);
  location = inject(Location);
  private route = inject(ActivatedRoute);

  constructor(private fb: FormBuilder) {
    this.masterForm = this.fb.group({
      basicInfo: this.fb.group({
        legalName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        phone: ['', [Validators.required, Validators.minLength(11), Validators.maxLength(11)]],
        personType: ['NATURAL', Validators.required],
      }),

      address: this.fb.group({
        zipCode: ['', Validators.required],
        street: ['', Validators.required],
        number: [''],
        city: ['', Validators.required],
        state: ['', Validators.required],
        neighborhood: ['', Validators.required],
      }),

      specificData: this.fb.group({
        cpf: [''],
        birthDate: [''],
        gender: [''],
        cnpj: [''],
        tradeName: [''],
        foundationDate: [''],
      }),
    });
  }

  ngOnInit() {
    this.configDynamicsValidations();
    this.checkIfIsEditing();
  }

  get basicInfoGroup(): FormGroup {
    return this.masterForm.get('basicInfo') as FormGroup;
  }

  get addressGroup(): FormGroup {
    return this.masterForm.get('address') as FormGroup;
  }

  get specificDataGroup(): FormGroup {
    return this.masterForm.get('specificData') as FormGroup;
  }

  get personType(): string {
    return this.basicInfoGroup.get('personType')?.value;
  }

  private configDynamicsValidations() {
    this.altValidations('NATURAL');

    this.basicInfoGroup.get('personType')?.valueChanges.subscribe((tipo) => {
      this.specificDataGroup.reset();
      this.altValidations(tipo);
    });
  }

  private altValidations(tipo: string) {
    const specific = this.specificDataGroup;

    if (tipo === 'NATURAL') {
      specific.get('cpf')?.setValidators([Validators.required, CustomValidators.cpf]);
      specific.get('birthDate')?.setValidators([Validators.required, CustomValidators.birthDate]);
      specific.get('gender')?.setValidators([Validators.required]);

      specific.get('cnpj')?.clearValidators();
      specific.get('tradeName')?.clearValidators();
      specific.get('foundationDate')?.clearValidators();
    } else {
      specific.get('cnpj')?.setValidators([Validators.required, CustomValidators.cnpj]);
      specific.get('tradeName')?.setValidators([Validators.required]);
      specific.get('foundationDate')?.setValidators([Validators.required]);

      specific.get('cpf')?.clearValidators();
      specific.get('birthDate')?.clearValidators();
      specific.get('gender')?.clearValidators();
    }

    Object.keys(specific.controls).forEach((key) => {
      specific.get(key)?.updateValueAndValidity();
    });
  }

  nextPage() {
    if (this.currentPage < this.pages) this.currentPage++;
  }

  backPage() {
    if (this.currentPage > 1) this.currentPage--;
  }

  save() {
    if (this.masterForm.valid) {
      this.isSubmitting = true;

      const rawValues = this.masterForm.getRawValue();
      const payload = {
        ...rawValues.basicInfo,
        address: rawValues.address,
        ...rawValues.specificData,
      };

      if (this.isEditing && this.clientId) {
        this.clientService.update(this.clientId, payload).subscribe({
          next: () => {
            this.toastService.showSuccess('Cliente atualizado com sucesso!');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            this.isSubmitting = false;
          },
        });
      } else {
        this.clientService.insert(payload).subscribe({
          next: () => {
            this.toastService.showSuccess('Cliente cadastrado com sucesso!');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            this.isSubmitting = false;
          },
        });
      }
    } else {
      this.masterForm.markAllAsTouched();
      this.toastService.showError('Preencha todos os campos obrigatórios corretamente.');
    }
  }

  cancel() {
    this.location.back();
  }

  private checkIfIsEditing() {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.isEditing = true;
      this.clientId = Number(idParam);

      this.clientService.findById(this.clientId).subscribe({
        next: (client) => {
          const isNatural = client.personType === 'NATURAL';
          const natural = client as INaturalClientDto;
          const juridic = client as IJuridicClientDto;

          this.masterForm.patchValue({
            basicInfo: {
              legalName: client.legalName,
              email: client.email,
              phone: client.phone,
              personType: client.personType,
            },
            address: client.address,
            specificData: {
              cpf: isNatural ? natural.cpf : null,
              cnpj: isNatural ? null : juridic.cnpj,
              birthDate: isNatural ? natural.birthDate : null,
              foundationDate: isNatural ? null : juridic.foundationDate,
              gender: isNatural ? natural.gender : null,
              tradeName: isNatural ? null : juridic.tradeName,
            },
          });

          this.masterForm.get('basicInfo.personType')?.disable();
          this.masterForm.get('specificData.cpf')?.disable();
          this.masterForm.get('specificData.cnpj')?.disable();
        },
        error: () => this.toastService.showError('Erro ao carregar dados do cliente.'),
      });
    }
  }
}
