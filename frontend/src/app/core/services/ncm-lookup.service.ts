import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';

const BRASIL_API_NCM_URL = 'https://brasilapi.com.br/api/ncm/v1';

export interface NcmItem {
  codigo: string;
  descricao: string;
  data_inicio?: string;
  data_fim?: string;
  tipo_ato?: string;
  numero_ato?: string;
  ano_ato?: string;
}

@Injectable({ providedIn: 'root' })
export class NcmLookupService {
  private readonly http = inject(HttpClient);

  /**
   * Pesquisa NCMs por palavra-chave na descrição ou código parcial via BrasilAPI.
   */
  searchByTerm(term: string): Observable<NcmItem[]> {
    const cleanTerm = term ? term.trim() : '';
    if (cleanTerm.length < 3) {
      return of([]);
    }

    return this.http.get<NcmItem[]>(`${BRASIL_API_NCM_URL}?search=${encodeURIComponent(cleanTerm)}`).pipe(
      map((response) => (Array.isArray(response) ? response : [])),
      catchError(() => of([])),
    );
  }

  /**
   * Consulta os dados detalhados de um código NCM específico (8 dígitos) via BrasilAPI.
   */
  lookupByCode(code: string): Observable<NcmItem | null> {
    const digits = this.clean(code);
    if (digits.length !== 8) {
      return of(null);
    }

    return this.http.get<NcmItem>(`${BRASIL_API_NCM_URL}/${digits}`).pipe(
      catchError(() => of(null)),
    );
  }

  /**
   * Remove pontuações e caracteres não numéricos.
   */
  clean(code: string): string {
    return code ? code.replace(/\D/g, '') : '';
  }
}
