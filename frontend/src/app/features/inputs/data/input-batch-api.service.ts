import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../../../core/models/page.model';
import {
  InputBatch,
  InputBatchInsert,
  InputBatchTableItem,
  InputBatchUpdate,
} from '../models/input-batch.model';

@Injectable({ providedIn: 'root' })
export class InputBatchApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/input-batches`;

  findPaged(
    page: number = 0,
    size: number = 10,
    searchTerm: string = '',
  ): Observable<Page<InputBatchTableItem>> {
    const params = new HttpParams({
      fromObject: {
        page,
        size,
        ...(searchTerm ? { searchTerm } : {}),
      },
    });

    return this.http.get<Page<InputBatchTableItem>>(`${this.apiUrl}/paged`, { params });
  }

  findById(id: number): Observable<InputBatch> {
    return this.http.get<InputBatch>(`${this.apiUrl}/${id}`);
  }

  insert(dto: InputBatchInsert): Observable<InputBatch> {
    return this.http.post<InputBatch>(this.apiUrl, dto);
  }

  update(id: number, dto: InputBatchUpdate): Observable<InputBatch> {
    return this.http.patch<InputBatch>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/activate`, {});
  }
}
