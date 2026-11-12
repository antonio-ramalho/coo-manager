import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { of } from 'rxjs';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { SalesDashboardComponent } from './sales-dashboard.component';
import { SalesApiService } from '../../data/sales-api.service';
import { SalesDashboardDto } from '../../models/sales-dashboard.model';

registerLocaleData(localePt);

describe('SalesDashboardComponent', () => {
  let component: SalesDashboardComponent;
  let fixture: ComponentFixture<SalesDashboardComponent>;
  let mockSalesApiService: { getSalesDashboard: ReturnType<typeof vi.fn> };

  const mockDashboardData: SalesDashboardDto = {
    averageTicket: 450.5,
    expirationRiskRate: 15.2,
    monthlySales: [
      { year: 2026, month: 9, monthLabel: 'Set/2026', totalValue: 12000, salesCount: 25 },
      { year: 2026, month: 10, monthLabel: 'Out/2026', totalValue: 15400, salesCount: 32 },
    ],
    topSellingProducts: [
      { productId: 1, productName: 'Adubo Orgânico', totalQuantitySold: 300, measureUnit: 'KILOGRAM' },
      { productId: 2, productName: 'Biofertilizante', totalQuantitySold: 120, measureUnit: 'LITER' },
    ],
  };

  beforeEach(async () => {
    mockSalesApiService = {
      getSalesDashboard: vi.fn().mockReturnValue(of(mockDashboardData)),
    };

    await TestBed.configureTestingModule({
      imports: [SalesDashboardComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        provideCharts(withDefaultRegisterables()),
        { provide: LOCALE_ID, useValue: 'pt-BR' },
        {
          provide: BreakpointObserver,
          useValue: { observe: () => of({ matches: true, breakpoints: {} }) },
        },
        { provide: SalesApiService, useValue: mockSalesApiService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve instanciar o componente e consumir os dados de BI', () => {
    expect(component).toBeTruthy();
    expect(mockSalesApiService.getSalesDashboard).toHaveBeenCalled();
  });

  it('deve desativar o loading após o carregamento dos dados de dashboard', () => {
    expect(component.loading).toBe(false);
  });
});

