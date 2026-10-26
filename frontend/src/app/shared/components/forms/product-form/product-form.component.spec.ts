import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideEnvironmentNgxMask } from 'ngx-mask';
import { of } from 'rxjs';
import { describe, expect, it, vi } from 'vitest';
import { ProductFormComponent } from './product-form.component';
import { buildProductForm } from '../../../../features/inputs/forms/product-form.builder';
import { NcmLookupService } from '../../../../core/services/ncm-lookup.service';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;
  let ncmService: NcmLookupService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFormComponent, ReactiveFormsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideEnvironmentNgxMask(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    ncmService = TestBed.inject(NcmLookupService);

    const fb = TestBed.inject(FormBuilder);
    component.group = buildProductForm(fb);
    fixture.detectChanges();
  });

  it('should create the component and have required form controls', () => {
    expect(component).toBeTruthy();
    expect(component.group.get('productName')).toBeTruthy();
    expect(component.group.get('productCode')).toBeTruthy();
    expect(component.group.get('measureUnit')).toBeTruthy();
    expect(component.group.get('price')).toBeTruthy();
    expect(component.group.get('ncm')).toBeTruthy();
  });

  it('should select an NCM suggestion and populate form control', () => {
    const mockNcm = { codigo: '31010000', descricao: 'Adubos de origem animal ou vegetal' };

    component.selectNcm(mockNcm);

    expect(component.group.get('ncm')?.value).toBe('31010000');
    expect(component.selectedNcmDescription()).toBe('Adubos de origem animal ou vegetal');
    expect(component.ncmSuggestions().length).toBe(0);
  });

  it('should auto-fill NCM input when product name query returns results', () => {
    const mockNcm = [{ codigo: '07099300', descricao: 'Abobrinha verde' }];
    vi.spyOn(ncmService, 'searchByTerm').mockReturnValue(of(mockNcm));

    component.group.get('productName')?.setValue('Abobrinha');
    component['listenToProductNameChanges']();
    expect(component.group.get('productName')?.value).toBe('Abobrinha');
  });

  it('should close suggestions when closeSuggestions() is called', () => {
    component.ncmSuggestions.set([{ codigo: '12345678', descricao: 'Teste' }]);
    expect(component.ncmSuggestions().length).toBe(1);

    component.closeSuggestions();
    expect(component.ncmSuggestions().length).toBe(0);
  });
});
