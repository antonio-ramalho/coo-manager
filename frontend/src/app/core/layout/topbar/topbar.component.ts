import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { ConnectedPosition, OverlayModule } from '@angular/cdk/overlay';
import {
  LucideBell,
  LucideChevronDown,
  LucideLogOut,
  LucideMenu,
  LucideSearch,
  LucideSettings,
  LucideUser,
} from '@lucide/angular';
import { SubNavComponent } from '../../../shared/components/layout/sub-nav/sub-nav.component';
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
  imports: [
    LucideMenu,
    LucideSearch,
    LucideBell,
    LucideUser,
    LucideSettings,
    LucideChevronDown,
    LucideLogOut,
    OverlayModule,
    SubNavComponent,
  ],
  templateUrl: './topbar.component.html',
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

  isUserMenuOpen = false;

  readonly overlayPositions: ConnectedPosition[] = [
    {
      originX: 'end',
      originY: 'bottom',
      overlayX: 'end',
      overlayY: 'top',
      offsetY: 8,
    },
    {
      originX: 'start',
      originY: 'bottom',
      overlayX: 'start',
      overlayY: 'top',
      offsetY: 8,
    },
  ];

  toggleUserMenu(): void {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  closeUserMenu(): void {
    this.isUserMenuOpen = false;
  }

  navigateTo(route: string): void {
    this.closeUserMenu();
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
    this.closeUserMenu();
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
