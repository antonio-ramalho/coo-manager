import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { SalesDashboardDto } from '../models/sales-dashboard.model';
import { SalesDto, SalesInsertDto } from '../models/sales-transaction.model';

@Injectable({ providedIn: 'root' })
export class SalesApiService {
  private readonly http = inject(HttpClient);
  private readonly purchaseUrl = `${environment.apiUrl}/purchase`;
  private readonly dashboardUrl = `${environment.apiUrl}/input-batches/dashboard/sales`;

  processSale(dto: SalesInsertDto): Observable<SalesDto> {
    return this.http.post<SalesDto>(this.purchaseUrl, dto);
  }

  getSalesDashboard(): Observable<SalesDashboardDto> {
    return this.http.get<SalesDashboardDto>(this.dashboardUrl);
  }

  getSaleById(id: number): Observable<SalesDto> {
    return this.http.get<SalesDto>(`${this.purchaseUrl}/${id}`);
  }

  cancelSale(id: number): Observable<void> {
    return this.http.delete<void>(`${this.purchaseUrl}/${id}`);
  }

  registerPayment(id: number): Observable<SalesDto> {
    return this.http.post<SalesDto>(`${this.purchaseUrl}/${id}/pay`, {});
  }
}
