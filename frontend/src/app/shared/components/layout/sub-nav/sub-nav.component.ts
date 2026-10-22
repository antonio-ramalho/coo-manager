import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

export interface SubNavTab {
  label: string;
  route: string;
}

/**
 * Dumb Component para navegação por abas secundárias em páginas de contexto (ex: Clientes, Insumos).
 * Suporta rolagem horizontal fluida em telas pequenas.
 */
@Component({
  selector: 'app-sub-nav',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sub-nav.component.html',
})
export class SubNavComponent {
  @Input({ required: true }) tabs: SubNavTab[] = [];
}
