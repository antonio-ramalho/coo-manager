import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, PageHeaderComponent, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './client.component.html',
})
export class ClientsComponent {
}
