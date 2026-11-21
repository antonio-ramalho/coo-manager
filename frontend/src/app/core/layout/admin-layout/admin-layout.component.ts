import { Component, DestroyRef, computed, inject, signal } from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { filter, map } from 'rxjs';

import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';
import { NAVIGATION_SECTIONS } from '../navigation.config';
import { AuthService } from '../../services/auth.service';

/**
 * Smart Component (App Shell) do painel administrativo.
 * Centraliza a responsividade pesada (Sidebar colapsável no desktop, Drawer no mobile).
 */
@Component({
  selector: 'app-admin-layout',
  imports: [RouterOutlet, SidebarComponent, TopbarComponent],
  templateUrl: './admin-layout.component.html',
})
export class AdminLayoutComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);
  private readonly authService = inject(AuthService);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  readonly isSidebarCollapsed = signal<boolean>(false);
  readonly isMobileMenuOpen = signal<boolean>(false);

  readonly sections = computed(() => {
    return NAVIGATION_SECTIONS.map((section) => ({
      ...section,
      items: section.items.filter((item) => {
        if (!item.permission && (!item.permissions || item.permissions.length === 0)) {
          return true;
        }
        if (item.permission) {
          return this.authService.hasPermission(item.permission);
        }
        return item.permissions!.some((p) => this.authService.hasPermission(p));
      }),
    })).filter((section) => section.items.length > 0);
  });

  constructor() {
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef),
      )
      .subscribe(() => {
        this.closeMobileMenu();
      });
  }

  toggleCollapse(): void {
    this.isSidebarCollapsed.update((collapsed) => !collapsed);
  }

  openMobileMenu(): void {
    this.isMobileMenuOpen.set(true);
  }

  closeMobileMenu(): void {
    this.isMobileMenuOpen.set(false);
  }
}
