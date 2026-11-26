import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ChangePasswordRequest } from '../models/change-password.model';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/users`;

  changePassword(userId: number, request: ChangePasswordRequest): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${userId}/change-password`, request);
  }
}
