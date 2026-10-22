import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Page } from '../../../core/models/page.model';
import { Client, ClientPayload, ClientSummary } from '../models/client.model';

/** Acesso HTTP ao recurso `/clients` do backend. */
@Injectable({ providedIn: 'root' })
export class ClientApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/clients`;

  /** Lista paginada (página base 0) com busca opcional por nome, CPF ou CNPJ. */
  findAllPaged(page: number, size: number, searchTerm = ''): Observable<Page<ClientSummary>> {
    const params = new HttpParams({ fromObject: { page, size, ...(searchTerm ? { searchTerm } : {}) } });
    return this.http.get<Page<ClientSummary>>(this.apiUrl, { params });
  }

  findById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  insert(client: ClientPayload): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  update(id: number, client: ClientPayload): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  /** Desativação lógica (o backend apenas altera o status). */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, {});
  }
}
