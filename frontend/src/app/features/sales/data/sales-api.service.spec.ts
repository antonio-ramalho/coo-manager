import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../../../environments/environment';
import { SalesApiService } from './sales-api.service';
import { SalesDashboardDto } from '../models/sales-dashboard.model';
import { RecentSaleItem, SalesDto, SalesInsertDto, SalesTableItem } from '../models/sales-transaction.model';
import { Page } from '../../../core/models/page.model';

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
      uuid: '550e8400-e29b-41d4-a716-446655440000',
      clientName: 'João da Silva',
      clientDocument: '12345678900',
      clientBirthDate: '1985-05-10',
      clientStatus: 'ACTIVE',
      totalValue: 250.0,
      purchaseDate: '2026-10-26',
      paymentStatus: 'PENDING',
      paymentStatusDescription: 'Pendente',
      items: [
        {
          productName: 'Adubo Orgânico',
          batchCode: 'LOT-001',
          measureUnit: 'KILOGRAM',
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

  it('deve buscar vendas paginadas com parâmetros corretos', () => {
    const mockPage: Page<SalesTableItem> = {
      content: [
        {
          id: 1,
          uuid: '550e8400-e29b-41d4-a716-446655440000',
          clientName: 'João da Silva',
          purchaseDate: '2026-10-26T10:00:00',
          totalValue: 250.0,
          paymentStatus: 'PENDING',
          paymentStatusDescription: 'Pendente',
        },
      ],
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true,
      pageable: { pageNumber: 0, pageSize: 10 },
    };

    service.findPaged(0, 10, 'Silva').subscribe((res) => {
      expect(res).toEqual(mockPage);
    });

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/paged?page=0&size=10&searchTerm=Silva`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPage);
  });

  it('deve buscar últimas 5 vendas sem parâmetro de dias', () => {
    const mockRecent: RecentSaleItem[] = [
      {
        id: 1,
        productName: 'Biofertilizante Líquido',
        purchaseDate: '2026-10-26T10:00:00',
        totalValue: 250.0,
      },
    ];

    service.findRecent().subscribe((res) => {
      expect(res).toEqual(mockRecent);
    });

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/recent`);
    expect(req.request.method).toBe('GET');
    req.flush(mockRecent);
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

  it('deve registrar pagamento com sucesso via PATCH', () => {
    service.registerPayment(100).subscribe();

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/100`);
    expect(req.request.method).toBe('PATCH');
    req.flush({ id: 100, paymentStatus: 'PAID' });
  });

  it('deve cancelar venda via DELETE', () => {
    service.cancelSale(100).subscribe();

    const req = httpTesting.expectOne(`${environment.apiUrl}/purchase/100`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
