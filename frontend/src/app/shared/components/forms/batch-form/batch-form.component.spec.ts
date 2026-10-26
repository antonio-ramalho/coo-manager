import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { provideFlatpickrDefaults } from 'angularx-flatpickr';
import { BatchFormComponent } from './batch-form.component';
import { buildBatchForm } from '../../../../features/inputs/forms/batch-form.builder';

describe('BatchFormComponent', () => {
  let component: BatchFormComponent;
  let fixture: ComponentFixture<BatchFormComponent>;
  let fb: FormBuilder;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BatchFormComponent, ReactiveFormsModule],
      providers: [provideFlatpickrDefaults()],
    }).compileComponents();

    fixture = TestBed.createComponent(BatchFormComponent);
    component = fixture.componentInstance;
    fb = TestBed.inject(FormBuilder);
  });

  it('should create the component and have required form controls', () => {
    component.group = buildBatchForm(fb, true);
    fixture.detectChanges();

    expect(component).toBeTruthy();
    expect(component.group.get('batchCode')).toBeTruthy();
    expect(component.group.get('quantityValue')).toBeTruthy();
    expect(component.group.get('expirationDate')).toBeTruthy();
    expect(component.group.get('inputProductId')).toBeTruthy();
  });

  it('should reflect showProductSelect input', () => {
    component.group = buildBatchForm(fb, true);
    component.showProductSelect = true;
    component.products = [{ id: 1, productName: 'Insumo Teste', productCode: 'INS-01', productCatalogStatus: 'ACTIVE' }];
    fixture.detectChanges();

    const selectEl = fixture.nativeElement.querySelector('#inputProductId');
    expect(selectEl).toBeTruthy();
  });
});
