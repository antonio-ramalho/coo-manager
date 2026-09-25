import { HeaderLayoutComponent } from '../../shared/layouts/header-layout/header-layout.component';
import { Component } from '@angular/core';
import { PersonForm } from '../../shared/components/forms/person-form/person-form-component';
import { AddressFormComponent } from '../../shared/components/forms/address-form/address-form-component';

@Component({
  selector: 'app-financial',
  standalone: true,
  imports: [HeaderLayoutComponent],
  templateUrl: './financial.component.html',
  styleUrl: './financial.component.css',
})
export class FinancialComponent {}
