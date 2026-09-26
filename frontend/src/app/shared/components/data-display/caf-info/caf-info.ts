import { Component, Input } from '@angular/core';
import { LucideCalendar, LucideFileText } from '@lucide/angular';

@Component({
  selector: 'app-farmer-caf-generic-info',
  imports: [
    LucideCalendar,
    LucideFileText
  ],
  templateUrl: './caf-info.html',
})
export class FarmerCafGenericInfoComponent {

  @Input() caf?: string;
  @Input() validade?: string;
}
