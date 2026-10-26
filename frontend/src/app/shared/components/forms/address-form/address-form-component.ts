import { Component, DestroyRef, ElementRef, inject, Input, OnInit, viewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, filter, switchMap } from 'rxjs';
import { NgxMaskDirective } from 'ngx-mask';
import { InputErrorComponent } from '../input-error/input-error-component';
import { BrazilianState } from '../../../enums/brazilian-state';
import { ZipCodeLookupService } from '../../../../core/services/zip-code-lookup.service';

const ZIP_CODE_DEBOUNCE_MS = 300;

/**
 * Campos de endereço ligados a um `FormGroup` recebido do pai.
 * Usa apenas o serviço de infraestrutura de CEP (sem regras de negócio) para autopreencher o endereço.
 */
@Component({
  selector: 'app-address-form',
  imports: [ReactiveFormsModule, NgxMaskDirective, InputErrorComponent],
  templateUrl: './address-form-component.html',
})
export class AddressFormComponent implements OnInit {
  @Input({ required: true }) group!: FormGroup;

  private readonly zipCodeLookup = inject(ZipCodeLookupService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly numberInput = viewChild<ElementRef<HTMLInputElement>>('numberInput');

  readonly states = Object.entries(BrazilianState);

  ngOnInit(): void {
    this.group
      .get('zipCode')
      ?.valueChanges.pipe(
        debounceTime(ZIP_CODE_DEBOUNCE_MS),
        filter((zipCode: string | null) => this.zipCodeLookup.isComplete(zipCode)),
        switchMap((zipCode: string) => this.zipCodeLookup.lookup(zipCode)),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe((address) => {
        this.group.patchValue(address);
        this.numberInput()?.nativeElement.focus();
      });
  }
}
