import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderLayoutComponent } from '../../shared/layouts/header-layout/header-layout.component';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, HeaderLayoutComponent, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './client.component.html',
  styleUrls: ['client.component.scss'],
})
export class ClientsComponent {}
