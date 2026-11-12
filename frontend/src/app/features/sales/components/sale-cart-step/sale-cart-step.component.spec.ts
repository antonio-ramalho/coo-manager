import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { SaleCartStepComponent } from './sale-cart-step.component';
import { buildSalesForm } from '../../forms/sales-form.builder';
import { InputBatchTableItem } from '../../../inputs/models/input-batch.model';

describe('SaleCartStepComponent', () => {
  let fixture: ComponentFixture<SaleCartStepComponent>;
  let component: SaleCartStepComponent;
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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaleCartStepComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SaleCartStepComponent);
    component = fixture.componentInstance;
    component.formGroup = buildSalesForm(fb);
    component.searchBatchesFn = vi.fn().mockReturnValue(of([mockBatch]));
    fixture.detectChanges();
  });

  it('should format batch label with only product name and batch code', () => {
    expect(component.batchLabelFormatter(mockBatch)).toBe('Adubo Orgânico (LOTE-101)');
  });

  it('should toggle inline search row when adding or canceling a new item', () => {
    expect(component.isAddingItem()).toBe(false);

    component.startAddingItem();
    fixture.detectChanges();
    expect(component.isAddingItem()).toBe(true);
    expect(fixture.nativeElement.querySelector('#batchSearchInline')).toBeTruthy();

    component.cancelAddingItem();
    fixture.detectChanges();
    expect(component.isAddingItem()).toBe(false);
    expect(fixture.nativeElement.querySelector('#batchSearchInline')).toBeNull();
  });

  it('should emit batchSelected and close inline search row when choosing a batch', () => {
    const emitSpy = vi.spyOn(component.batchSelected, 'emit');
    component.startAddingItem();

    component.onBatchChosen(mockBatch);

    expect(emitSpy).toHaveBeenCalledWith(mockBatch);
    expect(component.isAddingItem()).toBe(false);
  });
});

