import { Component, inject } from '@angular/core';
import {HeaderLayoutComponent} from "../../shared/layouts/header-layout/header-layout.component";
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-inputs-products-component',
  imports: [HeaderLayoutComponent, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './inputs-products-component.html',
  styleUrl: './inputs-products-component.scss',
})
export class InputsProductsComponent {}
