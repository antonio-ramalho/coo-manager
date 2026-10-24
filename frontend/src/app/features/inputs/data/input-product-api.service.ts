import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../../../core/models/page.model';
import {
  InputProduct,
  InputProductInsert,
  InputProductSummary,
  InputProductUpdate,
} from '../models/input-product.model';

/**
 * Acesso HTTP ao recurso `/input-products`.
 * Preservado para a futura implementação da tela de insumos (ainda sem consumidores).
 */
@Injectable({ providedIn: 'root' })
export class InputProductApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/input-products`;

  findAllByPage(page: number, size = 10, searchTerm = ''): Observable<Page<InputProductSummary>> {
    const params = new HttpParams({ fromObject: { page, size, ...(searchTerm ? { searchTerm } : {}) } });
    return this.http.get<Page<InputProductSummary>>(this.apiUrl, { params });
  }

  findById(id: number): Observable<InputProduct> {
    return this.http.get<InputProduct>(`${this.apiUrl}/${id}`);
  }

  insert(dto: InputProductInsert): Observable<InputProduct> {
    return this.http.post<InputProduct>(this.apiUrl, dto);
  }

  update(id: number, dto: InputProductUpdate): Observable<InputProduct> {
    return this.http.patch<InputProduct>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/activate`, {});
  }
}
