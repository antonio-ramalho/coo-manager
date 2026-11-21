import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  CooperativeMin,
  CooperativeRequest,
  CooperativeResponse,
} from '../models/cooperative.models';
import { PageResponse } from '../models/employee.models';

@Injectable({
  providedIn: 'root',
})
export class CooperativeService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/cooperatives`;

  findAll(
    page: number = 0,
    size: number = 10,
    searchTerm: string = '',
  ): Observable<PageResponse<CooperativeMin>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (searchTerm) {
      params = params.set('searchTerm', searchTerm);
    }
    return this.http.get<PageResponse<CooperativeMin>>(this.apiUrl, { params });
  }

  findById(id: number): Observable<CooperativeResponse> {
    return this.http.get<CooperativeResponse>(`${this.apiUrl}/${id}`);
  }

  insert(dto: CooperativeRequest): Observable<CooperativeResponse> {
    return this.http.post<CooperativeResponse>(this.apiUrl, dto);
  }

  update(id: number, dto: CooperativeRequest): Observable<CooperativeResponse> {
    return this.http.put<CooperativeResponse>(`${this.apiUrl}/${id}`, dto);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}

