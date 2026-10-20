import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  InputProductDto,
  InputProductInsertDto,
  InputProductMinDto,
  InputProductUpdateDto,
} from '../interfaces/IInput-product';
import { IPage } from '../interfaces/IPage';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class InputProductApiService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/input-products`;

  findAllByPage(
    page: number,
    size: number = 10,
    searchTerm: string = '',
  ): Observable<IPage<InputProductMinDto>> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (searchTerm) {
      params = params.set('searchTerm', searchTerm);
    }

    return this.http.get<IPage<InputProductMinDto>>(this.apiUrl, { params });
  }

  findById(id: number): Observable<InputProductDto> {
    return this.http.get<InputProductDto>(`${this.apiUrl}/${id}`);
  }

  insert(dto: InputProductInsertDto): Observable<InputProductDto> {
    return this.http.post<InputProductDto>(this.apiUrl, dto);
  }

  update(id: number, dto: InputProductUpdateDto): Observable<InputProductDto> {
    return this.http.patch<InputProductDto>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
