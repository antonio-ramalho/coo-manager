import { Component, inject, Input } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, filter, Subject, switchMap, takeUntil } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NgxMaskDirective } from 'ngx-mask';
import { InputErrorComponent } from '../input-error/input-error-component';
import { BrazilianState } from '../../../enums/brazilianState';

@Component({
  selector: 'app-address-form',
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, InputErrorComponent],
  templateUrl: './address-form-component.html',
})
export class AddressFormComponent {
  @Input({ required: true }) group!: FormGroup;
  private destroy$ = new Subject<void>();

  http = inject(HttpClient);
  states = Object.entries(BrazilianState);

  ngOnInit() {
    this.group
      .get('zipCode')
      ?.valueChanges.pipe(
        takeUntil(this.destroy$),
        debounceTime(300),
        filter((cep) => cep && cep.replace(/\D/g, '').length === 8),
        switchMap((cep) => {
          const cepLimpo = cep.replace(/\D/g, '');
          return this.http.get<any>(`https://viacep.com.br/ws/${cepLimpo}/json/`);
        }),
      )
      .subscribe((dados) => {
        if (!dados.erro) {
          this.group.patchValue({
            street: dados.logradouro,
            city: dados.localidade,
            state: dados.uf,
            neighborhood: dados.bairro,
          });
          document.getElementById('number')?.focus();
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
