import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { provideFlatpickrDefaults } from 'angularx-flatpickr';
import { of } from 'rxjs';
import { describe, expect, it, vi } from 'vitest';
import { InputBatchFormComponent } from './input-batch-form.component';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';

describe('InputBatchFormComponent', () => {
  let component: InputBatchFormComponent;
  let fixture: ComponentFixture<InputBatchFormComponent>;
  let mockBatchService: { findById: ReturnType<typeof vi.fn>; insert: ReturnType<typeof vi.fn>; update: ReturnType<typeof vi.fn> };
  let mockProductService: { findAllByPage: ReturnType<typeof vi.fn> };
  let mockToastService: { showSuccess: ReturnType<typeof vi.fn>; showError: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    mockBatchService = {
      findById: vi.fn(),
      insert: vi.fn(),
      update: vi.fn(),
    };
    mockProductService = {
      findAllByPage: vi.fn().mockReturnValue(
        of({
          content: [{ id: 1, productName: 'Insumo Teste', productCode: 'INS-01', productCatalogStatus: 'ACTIVE' }],
          totalElements: 1,
          totalPages: 1,
          first: true,
          last: true,
          pageable: { pageNumber: 0, pageSize: 10 },
        }),
      ),
    };
    mockToastService = {
      showSuccess: vi.fn(),
      showError: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [InputBatchFormComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        provideFlatpickrDefaults(),
        { provide: InputBatchApiService, useValue: mockBatchService },
        { provide: InputProductApiService, useValue: mockProductService },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InputBatchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load active products', () => {
    expect(component).toBeTruthy();
    expect(mockProductService.findAllByPage).toHaveBeenCalled();
    expect(component.products().length).toBe(1);
  });

  it('should call batchService.insert on save when creating a valid batch', () => {
    component.batchGroup.patchValue({
      inputProductId: 1,
      batchCode: 'LOTE-2026-001',
      quantityValue: 100,
      expirationDate: '2026-12-31',
    });

    mockBatchService.insert.mockReturnValue(of({
      id: 5,
      batchCode: 'LOTE-2026-001',
      inputProductId: 1,
      productName: 'Insumo Teste',
      originalQuantity: 100,
      currentQuantity: 100,
      entryDate: '2026-10-25',
      expirationDate: '2026-12-31',
      status: 'IN_STOCK',
    }));

    component.save();

    expect(mockBatchService.insert).toHaveBeenCalled();
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Lote cadastrado com sucesso!');
  });
});
