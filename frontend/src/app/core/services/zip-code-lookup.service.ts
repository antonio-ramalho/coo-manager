import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { filter, map, Observable } from 'rxjs';
import { Address } from '../../shared/models/address.model';
import { BrazilianStateCode } from '../../shared/enums/brazilian-state';

const VIA_CEP_URL = 'https://viacep.com.br/ws';
const ZIP_CODE_LENGTH = 8;

/** Contrato bruto da API pública ViaCEP. */
interface ViaCepResponse {
  logradouro: string;
  bairro: string;
  localidade: string;
  uf: BrazilianStateCode;
  erro?: boolean | 'true';
}

/** Campos de endereço que o ViaCEP consegue preencher automaticamente. */
export type ZipCodeAddress = Pick<Address, 'street' | 'neighborhood' | 'city' | 'state'>;

/** Serviço de infraestrutura para autopreenchimento de endereço a partir do CEP. */
@Injectable({ providedIn: 'root' })
export class ZipCodeLookupService {
  private readonly http = inject(HttpClient);

  /** Indica se o valor contém um CEP completo (8 dígitos), com ou sem máscara. */
  isComplete(zipCode: string | null | undefined): zipCode is string {
    return !!zipCode && this.clean(zipCode).length === ZIP_CODE_LENGTH;
  }

  /** Busca o endereço do CEP. CEPs inexistentes não emitem valor. */
  lookup(zipCode: string): Observable<ZipCodeAddress> {
    return this.http.get<ViaCepResponse>(`${VIA_CEP_URL}/${this.clean(zipCode)}/json/`).pipe(
      filter((response) => !response.erro),
      map((response) => ({
        street: response.logradouro,
        neighborhood: response.bairro,
        city: response.localidade,
        state: response.uf,
      })),
    );
  }

  private clean(zipCode: string): string {
    return zipCode.replace(/\D/g, '');
  }
}
