import { Component } from '@angular/core';
import { ClientSummaryComponent } from './components/client-summary/client-summary-card-component';
import { CommonModule } from '@angular/common';
import { LucideChartColumn } from '@lucide/angular';

@Component({
  selector: 'app-customer-demographics',
  standalone: true,
  imports: [CommonModule, LucideChartColumn, ClientSummaryComponent],
  templateUrl: './customer-demographics.component.html',
})
export class CustomerDemographicsComponent {}
