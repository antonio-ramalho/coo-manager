import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { LucideBell, LucideMenu, LucideSearch, LucideUser } from '@lucide/angular';
import { SubNavComponent } from '../../../shared/components/layout/sub-nav/sub-nav.component';
import { SubNavService } from '../../services/sub-nav.service';

export interface CurrentUser {
  name: string;
  role: string;
  avatarUrl?: string;
}

/**
 * Dumb Component do topo da aplicação (Topbar).
 * Gerencia o botão hambúrguer no mobile, as abas do submenu contextual e dados do usuário.
 */
@Component({
  selector: 'app-topbar',
  imports: [LucideMenu, LucideSearch, LucideBell, LucideUser, SubNavComponent],
  templateUrl: './topbar.component.html',
})
export class TopbarComponent {
  private readonly subNavService = inject(SubNavService);
  readonly subNavTabs = this.subNavService.tabs;

  @Input() isDesktop = true;

  @Input() user: CurrentUser = {
    name: 'Antonio Ramalho',
    role: 'Administrador',
  };

  @Output() menuClick = new EventEmitter<void>();
}
