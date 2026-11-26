import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { of } from 'rxjs';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { BreakpointObserver } from '@angular/cdk/layout';
import { InputStockComponent } from './input-stock.component';

registerLocaleData(localePt);
import { InventoryApiService } from '../../data/inventory-api.service';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { InputBatch } from '../../models/input-batch.model';

describe('InputStockComponent (Modal Edition & Creation)', () => {
  let component: InputStockComponent;
  let fixture: ComponentFixture<InputStockComponent>;

  const mockInventoryService = {
    getDashboardSummary: vi.fn().mockReturnValue(of({
      totalStockValue: 12000,
      occupancy: 45,
    })),
    getExpiringBatches: vi.fn().mockReturnValue(of([])),
    getExpiredBatches: vi.fn().mockReturnValue(of([])),
  };

  const mockBatchService = {
    findPaged: vi.fn().mockReturnValue(of({
      content: [],
      totalElements: 0,
      pageable: { pageNumber: 0, pageSize: 10 },
    })),
    findById: vi.fn(),
    insert: vi.fn(),
    update: vi.fn(),
    delete: vi.fn(),
    activate: vi.fn(),
  };

  const mockProductService = {
    findAllByPage: vi.fn().mockReturnValue(of({ content: [], totalElements: 0 })),
  };

  const mockToastService = {
    showSuccess: vi.fn(),
    showError: vi.fn(),
  };

  const mockBreakpointObserver = {
    observe: vi.fn().mockReturnValue(of({ matches: true })),
  };

  const sampleBatch: InputBatch = {
    id: 5,
    batchCode: 'LOTE-2026-01',
    inputProductId: 10,
    productName: 'Adubo Fosfatado',
    currentQuantity: 50,
    originalQuantity: 50,
    entryDate: '2026-01-01',
    expirationDate: '2026-12-31',
    status: 'IN_STOCK',
  };

  beforeEach(() => {
    vi.clearAllMocks();
    mockBatchService.findPaged.mockReturnValue(of({
      content: [],
      totalElements: 0,
      pageable: { pageNumber: 0, pageSize: 10 },
    }));
    mockInventoryService.getDashboardSummary.mockReturnValue(of({
      totalStockValue: 12000,
      occupancy: 45,
    }));
    mockInventoryService.getExpiringBatches.mockReturnValue(of([]));
    mockInventoryService.getExpiredBatches.mockReturnValue(of([]));

    TestBed.configureTestingModule({
      imports: [InputStockComponent],
      providers: [
        { provide: InventoryApiService, useValue: mockInventoryService },
        { provide: InputBatchApiService, useValue: mockBatchService },
        { provide: InputProductApiService, useValue: mockProductService },
        { provide: ToastService, useValue: mockToastService },
        { provide: BreakpointObserver, useValue: mockBreakpointObserver },
      ],
    });

    fixture = TestBed.createComponent(InputStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve inicializar com o modal de lote fechado', () => {
    expect(component.isBatchModalOpen()).toBe(false);
    expect(component.editingBatchId()).toBeNull();
  });

  it('deve abrir o modal para criação de novo lote', () => {
    component.openNewBatchModal();

    expect(component.isBatchModalOpen()).toBe(true);
    expect(component.editingBatchId()).toBeNull();
    expect(component.batchForm.pristine).toBe(true);
  });

  it('deve abrir o modal em modo de edição e carregar os dados do lote', () => {
    mockBatchService.findById.mockReturnValue(of(sampleBatch));

    component.onEditBatch(5);

    expect(component.isBatchModalOpen()).toBe(true);
    expect(component.editingBatchId()).toBe(5);
    expect(mockBatchService.findById).toHaveBeenCalledWith(5);
    expect(component.batchForm.get('batchCode')?.value).toBe('LOTE-2026-01');
    expect(component.batchForm.get('inputProductId')?.value).toBe(10);
  });

  it('deve cadastrar novo lote ao salvar sem editingBatchId', () => {
    mockBatchService.insert.mockReturnValue(of(sampleBatch));

    component.openNewBatchModal();
    component.batchForm.setValue({
      batchCode: 'LOTE-NOVO',
      inputProductId: 10,
      quantityValue: 100,
      expirationDate: '2026-11-30',
    });

    component.onSaveBatch();

    expect(mockBatchService.insert).toHaveBeenCalled();
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Lote cadastrado com sucesso!');
    expect(component.isBatchModalOpen()).toBe(false);
  });

  it('deve atualizar lote existente ao salvar com editingBatchId', () => {
    mockBatchService.findById.mockReturnValue(of(sampleBatch));
    mockBatchService.update.mockReturnValue(of(sampleBatch));

    component.onEditBatch(5);
    component.batchForm.patchValue({
      expirationDate: '2027-01-01',
    });

    component.onSaveBatch();

    expect(mockBatchService.update).toHaveBeenCalledWith(
      5,
      expect.objectContaining({
        expirationDate: '2027-01-01',
      }),
    );
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Lote atualizado com sucesso!');
    expect(component.isBatchModalOpen()).toBe(false);
  });
});
