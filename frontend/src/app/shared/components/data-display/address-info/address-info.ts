import { Component, Input } from '@angular/core';
import { LucideEarth, LucideMapPin, LucideMapPinHouse, LucidePackage, LucideRoad, LucideSatellite } from '@lucide/angular';
import { CepPipe } from '../../../pipes/cep-pipe';
import { StateDescriptionPipe } from '../../../pipes/state-description-pipe';

@Component({
  selector: 'app-person-address-info',
  imports: [
    LucideEarth,
    LucideMapPin,
    LucideRoad,
    LucidePackage,
    CepPipe,
    LucideMapPinHouse,
    LucideSatellite,
    StateDescriptionPipe,
  ],
  templateUrl: './address-info.html',
  styleUrl: './address-info.scss',
})
export class PersonAddressInfoComponent {
  @Input() zipCode?: string;
  @Input() street?: string;
  @Input() neighborhood!: string;
  @Input() city!: string;
  @Input() houseNumber?: string;
  @Input() state!: string;
}
