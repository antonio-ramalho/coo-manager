import { Component, inject } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { SubNavComponent, SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';

/**
 * Smart Component (Shell da feature de clientes).
 * Exibe o SubNav contextual caso a rota ativa não requisite ocultação (ex: cadastro/edição).
 */
@Component({
  selector: 'app-clients-shell',
  imports: [RouterOutlet, SubNavComponent],
  templateUrl: './clients-shell.component.html',
})
export class ClientsShellComponent {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly tabs: SubNavTab[] = [
    { label: 'Gestão de Clientes', route: 'gestao' },
  ];

  showSubNav = true;

  constructor() {
    this.checkSubNavVisibility();
    this.router.events
      .pipe(
        filter((event): event is NavigationEnd => event instanceof NavigationEnd),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        this.checkSubNavVisibility();
      });
  }

  private checkSubNavVisibility(): void {
    let currentRoute: ActivatedRoute | null = this.router.routerState.root;
    while (currentRoute?.firstChild) {
      currentRoute = currentRoute.firstChild;
    }
    const hide = currentRoute?.snapshot?.data?.['hideSubNav'] === true;
    this.showSubNav = !hide;
  }
}
