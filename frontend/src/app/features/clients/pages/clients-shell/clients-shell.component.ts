import { Component, DestroyRef, inject } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';
import { SubNavService } from '../../../../core/services/sub-nav.service';

/**
 * Smart Component (Shell da feature de clientes).
 * Registra o SubNav contextual na Topbar e oculta caso a rota ativa requisite (ex: cadastro/edição).
 */
@Component({
  selector: 'app-clients-shell',
  imports: [RouterOutlet],
  templateUrl: './clients-shell.component.html',
})
export class ClientsShellComponent {
  private readonly router = inject(Router);
  private readonly subNavService = inject(SubNavService);
  private readonly destroyRef = inject(DestroyRef);

  readonly tabs: SubNavTab[] = [
    { label: 'Gestão de clientes', route: '/home/clientes/gestao' },
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
