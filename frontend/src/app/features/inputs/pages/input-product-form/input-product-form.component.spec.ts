import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { provideEnvironmentNgxMask } from 'ngx-mask';
import { provideFlatpickrDefaults } from 'angularx-flatpickr';
import { of } from 'rxjs';
import { describe, expect, it, vi } from 'vitest';

import { InputProductFormComponent } from './input-product-form.component';
import { InputProductApiService } from '../../data/input-product-api.service';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { ToastService } from '../../../../core/services/toast.service';

describe('InputProductFormComponent', () => {
  let component: InputProductFormComponent;
  let fixture: ComponentFixture<InputProductFormComponent>;
  let mockProductService: { findById: ReturnType<typeof vi.fn>; insert: ReturnType<typeof vi.fn>; update: ReturnType<typeof vi.fn> };
  let mockBatchService: { insert: ReturnType<typeof vi.fn> };
  let mockToastService: { showSuccess: ReturnType<typeof vi.fn>; showError: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    mockProductService = {
      findById: vi.fn(),
      insert: vi.fn(),
      update: vi.fn(),
    };
    mockBatchService = {
      insert: vi.fn(),
    };
    mockToastService = {
      showSuccess: vi.fn(),
      showError: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [InputProductFormComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        provideEnvironmentNgxMask(),
        provideFlatpickrDefaults(),
        { provide: InputProductApiService, useValue: mockProductService },
        { provide: InputBatchApiService, useValue: mockBatchService },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(InputProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and default to 1 step without initial batch', () => {
    expect(component).toBeTruthy();
    expect(component.stepLabels.length).toBe(1);
    expect(component.stepLabels[0]).toBe('Dados do Insumo');
    expect(component.includeInitialBatch()).toBe(false);
  });

  it('should toggle initial batch and update steps to 2', () => {
    const event = { target: { checked: true } } as unknown as Event;
    component.toggleInitialBatch(event);

    expect(component.includeInitialBatch()).toBe(true);
    expect(component.stepLabels.length).toBe(2);
    expect(component.stepLabels[1]).toBe('Lote Inicial');
  });

  it('should call productService.insert on save when creating a valid product', () => {
    component.productGroup.patchValue({
      productName: 'Adubo Orgânico',
      productCode: 'ADUB-001',
      measureUnit: 'KILOGRAM',
      price: 55.5,
      ncm: '31010000',
    });

    mockProductService.insert.mockReturnValue(of({
      id: 10,
      productName: 'Adubo Orgânico',
      productCode: 'ADUB-001',
      measureUnit: 'KILOGRAM',
      ncm: '31010000',
      price: { value: 55.5 },
      productCatalogStatus: 'ACTIVE',
    }));

    component.save();

    expect(mockProductService.insert).toHaveBeenCalled();
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Insumo cadastrado com sucesso!');
  });
});
