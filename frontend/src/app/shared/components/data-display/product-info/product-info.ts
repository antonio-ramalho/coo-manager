import { Component, Input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { LucideBarcode, LucideCircleDollarSign, LucideScale, LucideTag } from '@lucide/angular';
import { MeasureUnitPipe } from '../../../pipes/measure-unit-pipe';
import { NcmPipe } from '../../../pipes/ncm-pipe';
import { MeasureUnit } from '../../../enums/measure-unit';

@Component({
  selector: 'app-product-basic-info-card',
  standalone: true,
  imports: [
    CurrencyPipe,
    MeasureUnitPipe,
    NcmPipe,
    LucideCircleDollarSign,
    LucideScale,
    LucideBarcode,
    LucideTag,
  ],
  templateUrl: './product-info.html',
  styleUrls: ['./product-info.scss'],
})
export class ProductBasicInfoCardComponent {
  @Input({ required: true }) price!: number;
  @Input({ required: true }) measureUnit!: MeasureUnit | string;
  @Input() ncm?: string;
  @Input() productCode?: string;
  @Input() id?: number;
}
