import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SideMenuComponent } from '../side-menu-layout/side-menu.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet, SideMenuComponent],
  templateUrl: './base-page.component.html',
})
export class BasePageComponent {}
