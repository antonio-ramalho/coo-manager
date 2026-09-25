import { HeaderLayoutComponent } from '../../shared/layouts/header-layout/header-layout.component';
import { Component } from '@angular/core';

@Component({
  selector: 'app-sales',
  standalone: true,
  imports: [HeaderLayoutComponent],
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.css',
})
export class SalesComponent {}
