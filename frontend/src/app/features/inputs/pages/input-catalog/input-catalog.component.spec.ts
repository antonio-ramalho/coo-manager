import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { of, throwError } from 'rxjs';
import { BreakpointObserver } from '@angular/cdk/layout';
import { provideEnvironmentNgxMask } from 'ngx-mask';
import { InputCatalogComponent } from './input-catalog.component';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { MeasureUnit } from '../../../../shared/enums/measure-unit';
import { InputProduct } from '../../models/input-product.model';

describe('InputCatalogComponent (Modal Edition & Creation)', () => {
  let component: InputCatalogComponent;
  let fixture: ComponentFixture<InputCatalogComponent>;

  const mockProductService = {
    findAllByPage: vi.fn().mockReturnValue(of({ content: [], totalElements: 0 })),
    findById: vi.fn(),
    insert: vi.fn(),
    update: vi.fn(),
    delete: vi.fn(),
    activate: vi.fn(),
  };

  const mockToastService = {
    showSuccess: vi.fn(),
    showError: vi.fn(),
  };

  const mockBreakpointObserver = {
    observe: vi.fn().mockReturnValue(of({ matches: true })),
  };

  const sampleProduct: InputProduct = {
    id: 10,
    productName: 'Adubo Fosfatado',
    productCode: 'ADB-001',
    measureUnit: MeasureUnit.KILOGRAM,
    price: {
      value: 45.5,
    },
    ncm: '3105.20.00',
    productCatalogStatus: 'ACTIVE',
    batches: [],
  };

  beforeEach(() => {
    vi.clearAllMocks();
    mockProductService.findAllByPage.mockReturnValue(of({ content: [], totalElements: 0 }));

    TestBed.configureTestingModule({
      imports: [InputCatalogComponent],
      providers: [
        provideEnvironmentNgxMask(),
        { provide: InputProductApiService, useValue: mockProductService },
        { provide: ToastService, useValue: mockToastService },
        { provide: BreakpointObserver, useValue: mockBreakpointObserver },
      ],
    });

    fixture = TestBed.createComponent(InputCatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve inicializar com o modal de produto fechado', () => {
    expect(component.isProductModalOpen()).toBe(false);
    expect(component.editingProductId()).toBeNull();
  });

  it('deve abrir o modal para criação de novo produto', () => {
    component.openProductModal();

    expect(component.isProductModalOpen()).toBe(true);
    expect(component.editingProductId()).toBeNull();
    expect(component.productForm.pristine).toBe(true);
  });

  it('deve abrir o modal em modo de edição e carregar os dados do produto', () => {
    mockProductService.findById.mockReturnValue(of(sampleProduct));

    component.editProduct(10);

    expect(component.isProductModalOpen()).toBe(true);
    expect(component.editingProductId()).toBe(10);
    expect(mockProductService.findById).toHaveBeenCalledWith(10);
    expect(component.productForm.get('productName')?.value).toBe('Adubo Fosfatado');
    expect(component.productForm.get('productCode')?.value).toBe('ADB-001');
  });

  it('deve cadastrar novo insumo ao salvar sem editingProductId', () => {
    mockProductService.insert.mockReturnValue(of(sampleProduct));

    component.openProductModal();
    component.productForm.setValue({
      productName: 'Adubo Orgânico',
      productCode: 'ADB-999',
      measureUnit: MeasureUnit.KILOGRAM,
      price: '50.00',
      ncm: '31052000',
    });

    component.onSaveProduct();

    expect(mockProductService.insert).toHaveBeenCalled();
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Insumo cadastrado com sucesso!');
    expect(component.isProductModalOpen()).toBe(false);
  });

  it('deve atualizar insumo existente ao salvar com editingProductId', () => {
    mockProductService.findById.mockReturnValue(of(sampleProduct));
    mockProductService.update.mockReturnValue(of(sampleProduct));

    component.editProduct(10);
    component.productForm.patchValue({
      productName: 'Adubo Alterado',
    });

    component.onSaveProduct();

    expect(mockProductService.update).toHaveBeenCalledWith(
      10,
      expect.objectContaining({
        productName: 'Adubo Alterado',
      }),
    );
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Insumo atualizado com sucesso!');
    expect(component.isProductModalOpen()).toBe(false);
  });
});
