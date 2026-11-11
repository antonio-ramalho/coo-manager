import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { describe, expect, it, vi, beforeEach } from 'vitest';
import { NewSaleComponent } from './new-sale.component';
import { ClientApiService } from '../../../clients/data/client-api.service';
import { InputBatchApiService } from '../../../inputs/data/input-batch-api.service';
import { SalesApiService } from '../../data/sales-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { InputBatchTableItem } from '../../../inputs/models/input-batch.model';
import { Status } from '../../../../shared/enums/status';
import { PersonType } from '../../../../shared/enums/person-type';
import { NaturalClient } from '../../../clients/models/client.model';
import { buildSalesItemForm } from '../../forms/sales-form.builder';

describe('NewSaleComponent & SalesFormBuilder', () => {
  let component: NewSaleComponent;
  let fixture: ComponentFixture<NewSaleComponent>;
  let mockClientService: { findAllPaged: ReturnType<typeof vi.fn>; findById: ReturnType<typeof vi.fn> };
  let mockBatchService: { findPaged: ReturnType<typeof vi.fn> };
  let mockSalesService: { processSale: ReturnType<typeof vi.fn> };
  let mockToastService: { showSuccess: ReturnType<typeof vi.fn>; showError: ReturnType<typeof vi.fn> };
  let router: Router;
  const fb = new FormBuilder();

  const mockBatch: InputBatchTableItem = {
    id: 10,
    batchCode: 'LOTE-101',
    productName: 'Adubo Orgânico',
    productCode: 'ADB-01',
    expirationDate: '2027-10-26',
    currentQuantity: 50,
    unitPrice: 20.0,
    measureUnit: 'KG',
    status: 'IN_STOCK',
    statusDescription: 'Em estoque',
  };

  const mockClient: NaturalClient = {
    id: 1,
    legalName: 'Carlos Silva',
    email: 'carlos@exemplo.com',
    phone: '47999998888',
    status: Status.ACTIVE,
    personType: PersonType.NATURAL,
    cpf: '12345678901',
    birthDate: '1985-05-15',
    gender: null as any,
    address: {
      zipCode: '89000000',
      street: 'Rua das Flores',
      number: '123',
      neighborhood: 'Centro',
      city: 'Florianópolis',
      state: 'SC',
    },
  };

  beforeEach(async () => {
    mockClientService = {
      findAllPaged: vi.fn().mockReturnValue(
        of({
          content: [{ id: 1, legalName: 'Carlos Silva', documentNumber: '12345678901', status: Status.ACTIVE }],
          totalElements: 1,
          totalPages: 1,
          size: 20,
          number: 0,
        }),
      ),
      findById: vi.fn().mockReturnValue(of(mockClient)),
    };

    mockBatchService = {
      findPaged: vi.fn().mockReturnValue(
        of({
          content: [mockBatch],
          totalElements: 1,
          totalPages: 1,
          size: 20,
          number: 0,
        }),
      ),
    };

    mockSalesService = {
      processSale: vi.fn().mockReturnValue(of({ id: 100, farmerName: 'Carlos Silva', totalValue: 40.0 })),
    };

    mockToastService = {
      showSuccess: vi.fn(),
      showError: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [NewSaleComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        { provide: ClientApiService, useValue: mockClientService },
        { provide: InputBatchApiService, useValue: mockBatchService },
        { provide: SalesApiService, useValue: mockSalesService },
        { provide: ToastService, useValue: mockToastService },
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    vi.spyOn(router, 'navigate').mockResolvedValue(true);

    fixture = TestBed.createComponent(NewSaleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve inicializar o formulário de venda com personId nulo e carrinho vazio', () => {
    expect(component.salesForm.controls.personId.value).toBeNull();
    expect(component.salesForm.controls.items.length).toBe(0);
    expect(component.salesForm.valid).toBe(false);
    expect(component.currentPage).toBe(1);
  });

  it('deve atualizar o personId e carregar os dados cadastrais enxutos ao selecionar um cliente', () => {
    component.onClientSelected({
      id: 1,
      legalName: 'Carlos Silva',
      documentNumber: '12345678901',
      status: Status.ACTIVE,
    });

    expect(component.salesForm.controls.personId.value).toBe(1);
    expect(mockClientService.findById).toHaveBeenCalledWith(1);
    expect(component.selectedClientPreview()?.legalName).toBe('Carlos Silva');
    expect(component.selectedClientPreview()?.documentNumber).toBe('12345678901');
    expect(component.selectedClientPreview()?.birthDate).toBe('1985-05-15');
    expect(component.selectedClientPreview()?.status).toBe(Status.ACTIVE);
  });

  it('não deve avançar para a etapa de carrinho se nenhum cliente foi selecionado', () => {
    component.nextPage();
    expect(component.currentPage).toBe(1);
    expect(component.currentStep()).toBe('client');
    expect(component.salesForm.controls.personId.touched).toBe(true);
  });

  it('deve avançar para a etapa de carrinho se o cliente estiver preenchido', () => {
    component.salesForm.controls.personId.setValue(1);
    component.nextPage();
    expect(component.currentPage).toBe(2);
    expect(component.currentStep()).toBe('cart');
  });

  it('deve voltar para a etapa de cliente através do backPage', () => {
    component.salesForm.controls.personId.setValue(1);
    component.nextPage();
    expect(component.currentPage).toBe(2);

    component.backPage();
    expect(component.currentPage).toBe(1);
    expect(component.currentStep()).toBe('client');
  });

  it('deve adicionar um lote ao FormArray e calcular o total da venda em tempo real', () => {
    component.onBatchSelected(mockBatch);

    expect(component.salesForm.controls.items.length).toBe(1);
    const itemCtrl = component.salesForm.controls.items.at(0);
    expect(itemCtrl.get('batchId')?.value).toBe(10);
    expect(itemCtrl.get('unitPrice')?.value).toBe(20.0);
    expect(itemCtrl.get('quantity')?.value).toBe(1);
    expect(itemCtrl.get('totalPrice')?.value).toBe(20.0);
    expect(component.totalSaleValue()).toBe(20.0);
  });

  it('deve recalcular os valores em tempo real quando a quantidade for alterada via valueChanges', () => {
    component.onBatchSelected(mockBatch);
    const itemCtrl = component.salesForm.controls.items.at(0);

    itemCtrl.get('quantity')?.setValue(5);

    expect(itemCtrl.get('totalPrice')?.value).toBe(100.0);
    expect(component.totalSaleValue()).toBe(100.0);
  });

  it('deve incrementar a quantidade se o mesmo lote for selecionado novamente', () => {
    component.onBatchSelected(mockBatch);
    component.onBatchSelected(mockBatch);

    expect(component.salesForm.controls.items.length).toBe(1);
    const itemCtrl = component.salesForm.controls.items.at(0);
    expect(itemCtrl.get('quantity')?.value).toBe(2);
    expect(component.totalSaleValue()).toBe(40.0);
  });

  it('deve remover item do FormArray e atualizar o total para zero', () => {
    component.onBatchSelected(mockBatch);
    expect(component.salesForm.controls.items.length).toBe(1);

    component.onRemoveItem(0);

    expect(component.salesForm.controls.items.length).toBe(0);
    expect(component.totalSaleValue()).toBe(0);
  });

  it('deve abrir e fechar o diálogo de confirmação com dados resumidos da venda', () => {
    component.salesForm.controls.personId.setValue(1);
    component.selectedClientPreview.set({
      id: 1,
      legalName: 'Carlos Silva',
      documentNumber: '123.456.789-01',
      status: 'ACTIVE',
    });
    component.onBatchSelected(mockBatch);

    component.openConfirmationDialog();

    expect(component.isDialogOpen()).toBe(true);
    expect(component.confirmationClientName).toBe('Carlos Silva');
    expect(component.confirmationItems.length).toBe(1);
    expect(component.confirmationItems[0].productName).toBe('Adubo Orgânico');

    component.closeConfirmationDialog();
    expect(component.isDialogOpen()).toBe(false);
  });

  it('deve submeter a venda ao confirmar no diálogo e redirecionar para a tabela de vendas', () => {
    component.salesForm.controls.personId.setValue(1);
    component.onBatchSelected(mockBatch);

    component.executeSaleSubmission();

    expect(mockSalesService.processSale).toHaveBeenCalledWith({
      farmerId: 1,
      items: [{ batchId: 10, quantity: 1 }],
    });
    expect(mockToastService.showSuccess).toHaveBeenCalledWith('Venda realizada com sucesso!');
    expect(router.navigate).toHaveBeenCalledWith(['/home/vendas']);
  });

  it('deve fechar diálogo e exibir erro via Toast quando a submissão falhar', () => {
    mockSalesService.processSale.mockReturnValue(
      throwError(() => ({ error: { message: 'Data inválida para a transação' } })),
    );

    component.salesForm.controls.personId.setValue(1);
    component.onBatchSelected(mockBatch);

    component.openConfirmationDialog();
    expect(component.isDialogOpen()).toBe(true);

    component.executeSaleSubmission();

    expect(component.isDialogOpen()).toBe(false);
    expect(component.isSubmitting()).toBe(false);
    expect(mockToastService.showError).toHaveBeenCalledWith('Data inválida para a transação');
  });

  it('factory buildSalesItemForm deve validar quantidade mínima e estoque máximo', () => {
    const itemGroup = buildSalesItemForm(fb, {
      batchId: 1,
      availableStock: 10,
      quantity: 15,
      unitPrice: 5,
    });

    expect(itemGroup.get('quantity')?.hasError('max')).toBe(true);

    itemGroup.get('quantity')?.setValue(0);
    expect(itemGroup.get('quantity')?.hasError('min')).toBe(true);

    itemGroup.get('quantity')?.setValue(5);
    expect(itemGroup.get('quantity')?.valid).toBe(true);
  });
});
