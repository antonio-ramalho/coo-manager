import { Component, DestroyRef, inject } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';
import { SubNavService } from '../../../../core/services/sub-nav.service';

/**
 * Shell da feature de Vendas/Saídas.
 * Registra o SubNav contextual na Topbar e oculta caso a rota ativa requisite.
 */
@Component({
  selector: 'app-sales-shell',
  imports: [RouterOutlet],
  templateUrl: './sales-shell.component.html',
  styleUrl: './sales-shell.component.scss',
})
export class SalesShellComponent {
  private readonly router = inject(Router);
  private readonly subNavService = inject(SubNavService);
  private readonly destroyRef = inject(DestroyRef);

  readonly tabs: SubNavTab[] = [
    { label: 'Vendas', route: '/home/vendas', exact: true },
    { label: 'Estatísticas', route: '/home/vendas/estatisticas', exact: true },
  ];

  constructor() {
    this.updateSubNav();
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        this.updateSubNav();
      });

    this.destroyRef.onDestroy(() => {
      this.subNavService.clearTabs();
    });
  }

  private updateSubNav(): void {
    let currentRoute: ActivatedRoute | null = this.router.routerState.root;
    while (currentRoute?.firstChild) {
      currentRoute = currentRoute.firstChild;
    }
    const hide = currentRoute?.snapshot?.data?.['hideSubNav'] === true;
    this.subNavService.setTabs(hide ? null : this.tabs);
  }
}
