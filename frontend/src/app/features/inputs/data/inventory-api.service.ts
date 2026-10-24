import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  ExpiringBatch,
  InventoryDashboard,
  TopSellingProduct,
} from '../models/inventory-dashboard.model';

@Injectable({ providedIn: 'root' })
export class InventoryApiService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/input-batch/dashboard`;

  getDashboardSummary(): Observable<InventoryDashboard> {
    return this.http.get<InventoryDashboard>(this.baseUrl);
  }

  getExpiringBatches(days: number): Observable<ExpiringBatch[]> {
    const params = new HttpParams().set('days', days);
    return this.http.get<ExpiringBatch[]>(`${this.baseUrl}/expiring`, { params });
  }

  getTopSellingProducts(periodDays: number): Observable<TopSellingProduct[]> {
    const params = new HttpParams().set('periodDays', periodDays);
    return this.http.get<TopSellingProduct[]>(`${this.baseUrl}/top-selling`, { params });
  }
}
