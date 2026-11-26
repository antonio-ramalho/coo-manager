import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  EmployeeMin,
  EmployeeRequest,
  EmployeeResponse,
  EmployeeUpdate,
  PageResponse,
  RoleRequest,
} from '../models/employee.models';
import { RoleAuth } from '../models/auth.models';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private readonly http = inject(HttpClient);
  private readonly usersUrl = `${environment.apiUrl}/users`;
  private readonly rolesUrl = `${environment.apiUrl}/roles`;

  findAllByCooperative(
    cooperativeId: number,
    page: number = 0,
    size: number = 10,
    searchTerm: string = '',
  ): Observable<PageResponse<EmployeeMin>> {
    let params = new HttpParams().set('page', page).set('size', size);
    if (searchTerm) {
      params = params.set('searchTerm', searchTerm);
    }
    return this.http.get<PageResponse<EmployeeMin>>(`${this.usersUrl}/cooperative/${cooperativeId}`, {
      params,
    });
  }

  findById(id: number): Observable<EmployeeResponse> {
    return this.http.get<EmployeeResponse>(`${this.usersUrl}/${id}`);
  }

  insert(dto: EmployeeRequest): Observable<EmployeeResponse> {
    return this.http.post<EmployeeResponse>(this.usersUrl, dto);
  }

  update(id: number, dto: EmployeeUpdate): Observable<EmployeeResponse> {
    return this.http.put<EmployeeResponse>(`${this.usersUrl}/${id}`, dto);
  }

  activate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.usersUrl}/${id}/activate`, {});
  }

  deactivate(id: number): Observable<void> {
    return this.http.patch<void>(`${this.usersUrl}/${id}/deactivate`, {});
  }

  getRolesForCooperative(cooperativeId: number): Observable<RoleAuth[]> {
    return this.http.get<RoleAuth[]>(`${this.rolesUrl}/cooperative/${cooperativeId}`);
  }

  saveRole(dto: RoleRequest): Observable<RoleAuth> {
    return this.http.post<RoleAuth>(this.rolesUrl, dto);
  }

  updateRole(id: number, dto: RoleRequest): Observable<RoleAuth> {
    return this.http.put<RoleAuth>(`${this.rolesUrl}/${id}`, dto);
  }

  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.rolesUrl}/${id}`);
  }
}

