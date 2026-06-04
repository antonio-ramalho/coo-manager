import { Component } from '@angular/core';
import {PageHeaderComponent} from "../../shared/components/page-header/page-header.component";

@Component({
  selector: 'app-logistics',
  standalone: true,
  imports: [PageHeaderComponent],
  templateUrl: './logistics.component.html',
  styleUrl: './logistics.component.css',
})
export class LogisticsComponent {}
