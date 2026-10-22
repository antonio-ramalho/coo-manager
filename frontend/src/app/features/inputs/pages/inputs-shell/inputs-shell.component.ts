import { Component, inject } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { SubNavComponent, SubNavTab } from '../../../../shared/components/layout/sub-nav/sub-nav.component';

/**
 * Shell da feature de Insumos.
 */
@Component({
  selector: 'app-inputs-shell',
  imports: [RouterOutlet, SubNavComponent],
  templateUrl: './inputs-shell.component.html',
})
export class InputsShellComponent {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly tabs: SubNavTab[] = [
    { label: 'Estoque', route: 'insumos-estoque' },
    { label: 'Catálogo', route: 'catalogo' },
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
