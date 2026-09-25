import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IClient, IUpdateClient, IClientMinDto } from '../interfaces/IClient';
import { IPage } from '../interfaces/IPage';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ClientApiService {
  private readonly API_URL = `${environment.apiUrl}/clients`;

  constructor(private http: HttpClient) {}

  findAllPaged(
    page: number,
    size: number,
    searchTerm: string = '',
  ): Observable<IPage<IClientMinDto>> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (searchTerm) {
      params = params.set('searchTerm', searchTerm);
    }

    return this.http.get<IPage<IClientMinDto>>(this.API_URL, { params });
  }

  findById(id: number): Observable<IClient> {
    return this.http.get<IClient>(`${this.API_URL}/${id}`);
  }

  insert(client: IClient): Observable<IClient> {
    return this.http.post<IClient>(this.API_URL, client);
  }

  update(id: number, client: IUpdateClient): Observable<IClient> {
    return this.http.put<IClient>(`${this.API_URL}/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API_URL}/${id}`, {});
  }
}
