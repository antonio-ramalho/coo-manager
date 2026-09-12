import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Client {
  id: number;
  name: string;
  cpfNumber: string;
  selecionado?: boolean;
}

@Injectable({
  providedIn: 'root',
})
export class ClienteService {
  private http = inject(HttpClient);

  private readonly API_URL = 'http://localhost:8080/farmers';

  obterClientes(): Observable<Client[]> {
    return this.http.get<Client[]>(this.API_URL);
  }
}
