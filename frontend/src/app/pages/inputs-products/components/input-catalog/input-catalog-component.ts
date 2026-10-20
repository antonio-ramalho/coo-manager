import { Component } from '@angular/core';
import {ButtonComponent} from "../../../../shared/components/ui/button/button-component";
import {RouterLink} from "@angular/router";
import { AsyncPipe } from '@angular/common';
import { AttachmentsCardComponent } from '../../../../shared/components/data-display/attachments-info/attachments-info';
import { DetailsLayoutComponent } from '../../../../shared/layouts/details-layout/details-layout-component';
import { GenericListComponent } from '../../../../shared/components/lists/generic-list/generic-list-component';
import { PersonAddressInfoComponent } from '../../../../shared/components/data-display/address-info/address-info';
import { PersonBasicInfoCardComponent } from '../../../../shared/components/data-display/person-info/person-info';
import {
  PersonDetailsHeaderComponent
} from '../../../../shared/components/data-display/person-details-header/person-details-header';
import { async } from 'rxjs';

@Component({
  selector: 'app-input-catalog-component',
  imports: [ButtonComponent, RouterLink, AsyncPipe, AttachmentsCardComponent, DetailsLayoutComponent, GenericListComponent, PersonAddressInfoComponent, PersonBasicInfoCardComponent, PersonDetailsHeaderComponent],
  templateUrl: './input-catalog-component.html',
  styleUrl: './input-catalog-component.scss',
})
export class InputCatalogComponent {
  protected readonly async = async;
}
