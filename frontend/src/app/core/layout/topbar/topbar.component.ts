import { Component, EventEmitter, Input, Output } from '@angular/core';
import { LucideBell, LucideMenu, LucideSearch, LucideUser } from '@lucide/angular';

export interface CurrentUser {
  name: string;
  role: string;
  avatarUrl?: string;
}

/**
 * Dumb Component do topo da aplicação (Topbar).
 * Gerencia o botão hambúrguer no mobile e dados do usuário.
 */
@Component({
  selector: 'app-topbar',
  imports: [LucideMenu, LucideSearch, LucideBell, LucideUser],
  templateUrl: './topbar.component.html',
})
export class TopbarComponent {
  @Input() user: CurrentUser = {
    name: 'Antonio Ramalho',
    role: 'Administrador',
  };

  @Output() menuClick = new EventEmitter<void>();
}
