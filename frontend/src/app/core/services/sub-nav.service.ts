import { Injectable, signal } from '@angular/core';
import { SubNavTab } from '../../shared/components/layout/sub-nav/sub-nav.component';

/**
 * Serviço reativo para orquestrar as abas do SubNav exibidas na Topbar principal.
 * Permite que páginas de casca de contexto (Clients, Inputs, etc.) registrem
 * e ocultem suas abas secundárias sem acoplamento direto de templates.
 */
@Injectable({
  providedIn: 'root',
})
export class SubNavService {
  private readonly _tabs = signal<SubNavTab[] | null>(null);
  readonly tabs = this._tabs.asReadonly();

  setTabs(tabs: SubNavTab[] | null): void {
    this._tabs.set(tabs);
  }

  clearTabs(): void {
    this._tabs.set(null);
  }
}
