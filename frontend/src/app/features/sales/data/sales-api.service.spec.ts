import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../../environments/environment';
import { SalesApiService } from './sales-api.service';
import { SalesDashboardDto } from '../models/sales-dashboard.model';
import { SalesDto, SalesInsertDto } from '../models/sales-transaction.model';

describe('SalesApiService', () => {
  let service: SalesApiService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        SalesApiService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(SalesApiService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('deve enviar requisição POST para registrar venda', () => {
    const payload: SalesInsertDto = {
      farmerId: 1,
      items: [{ batchId: 10, quantity: 5 }],
    };

    const mockResponse: SalesDto = {
      id: 100,
      farmerName: 'João da Silva',
      totalValue: 250.0,
      purchaseDate: '2026-10-26',
      paymentStatus: 'Pendente',
      items: [
        {
          productName: 'Adubo Orgânico',
          quantity: 5,
          unitPrice: 50.0,
          itemTotalValue: 250.0,
        },
      ],
    };

    service.processSale(payload).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(mockResponse);
  });

  it('deve buscar dados consolidados do dashboard de vendas via GET', () => {
    const mockDashboard: SalesDashboardDto = {
      averageTicket: 350.0,
      expirationRiskRate: 12.5,
      monthlySales: [
        { year: 2026, month: 10, monthLabel: 'Out/2026', totalValue: 5000.0, salesCount: 15 },
      ],
      topSellingProducts: [
        { productId: 1, productName: 'Biofertilizante', totalQuantitySold: 120.0, measureUnit: 'LITER' },
      ],
    };

    service.getSalesDashboard().subscribe((res) => {
      expect(res).toEqual(mockDashboard);
    });

    const req = httpTesting.expectOne(`${environment.apiUrl}/input-batches/dashboard/sales`);
    expect(req.request.method).toBe('GET');
    req.flush(mockDashboard);
  });

  it('deve registrar pagamento com sucesso via POST', () => {
    service.registerPayment(100).subscribe();

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/100/pay`);
    expect(req.request.method).toBe('POST');
    req.flush({ id: 100, paymentStatus: 'Pago' });
  });

  it('deve cancelar venda via DELETE', () => {
    service.cancelSale(100).subscribe();

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/100`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
