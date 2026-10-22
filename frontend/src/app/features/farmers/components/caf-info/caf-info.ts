import { Component, Input } from '@angular/core';
import { LucideCalendar, LucideFileText } from '@lucide/angular';

/**
 * Dumb Component para exibição do CAF (Cadastro Nacional da Agricultura Familiar) do produtor.
 */
@Component({
  selector: 'app-farmer-caf-generic-info',
  imports: [LucideCalendar, LucideFileText],
  templateUrl: './caf-info.html',
})
export class FarmerCafGenericInfoComponent {
  @Input() caf?: string;
  @Input() expirationDate?: string;
}
