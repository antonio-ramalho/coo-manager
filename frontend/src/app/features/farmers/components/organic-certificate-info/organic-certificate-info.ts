import { Component, Input } from '@angular/core';
import { LucideCalendar, LucideFileText } from '@lucide/angular';

@Component({
  selector: 'app-farmer-cert-organic-info',
  imports: [LucideFileText, LucideCalendar],
  templateUrl: './organic-certificate-info.html',
})
export class FarmerCertOrganicInfoComponent {
  @Input() number?: string;
  @Input() expiration?: string;
}
