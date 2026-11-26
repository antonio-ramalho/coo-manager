import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import {
  LucideBell,
  LucideChevronDown,
  LucideMenu,
  LucideSearch,
  LucideUser,
} from '@lucide/angular';
import { SubNavComponent } from '../../../shared/components/layout/sub-nav/sub-nav.component';
import { ActionMenuComponent } from '../../../shared/components/ui/action-menu/action-menu.component';
import { ActionMenuItem } from '../../../shared/components/ui/action-menu/action-menu-item.model';
import { SubNavService } from '../../services/sub-nav.service';
import { AuthService } from '../../services/auth.service';

export interface CurrentUser {
  name: string;
  role: string;
  avatarUrl?: string;
}

/**
 * Dumb Component do topo da aplicação (Topbar).
 * Gerencia o botão hambúrguer no mobile, as abas do submenu contextual e menu de perfil/sessão.
 */
@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [
    LucideMenu,
    LucideSearch,
    LucideBell,
    LucideUser,
    LucideChevronDown,
    SubNavComponent,
    ActionMenuComponent,
  ],
  templateUrl: './topbar.component.html',
  styleUrl: './topbar.component.scss',
})
export class TopbarComponent {
  private readonly subNavService = inject(SubNavService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly subNavTabs = this.subNavService.tabs;
  readonly currentUser = this.authService.currentUser;

  @Input() isDesktop = true;

  @Input() user: CurrentUser = {
    name: 'Administrador',
    role: 'Gestão Cooperativa',
  };

  @Output() menuClick = new EventEmitter<void>();

  getUserMenuItems(): ActionMenuItem[] {
    return [
      {
        label: 'Meu Perfil',
        action: () => this.navigateTo('/home/funcionarios'),
      },
      {
        label: 'Configurações',
        action: () => this.navigateTo('/home/cooperativa'),
      },
      {
        label: 'Sair da Conta',
        danger: true,
        dividerAbove: true,
        action: () => this.logout(),
      },
    ];
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  getUserName(): string {
    return this.currentUser()?.fullName || this.user.name;
  }

  getUserRole(): string {
    const roles = this.currentUser()?.roles;
    if (roles && roles.length > 0) {
      return roles.map((r) => r.name).join(', ');
    }
    return this.user.role;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
