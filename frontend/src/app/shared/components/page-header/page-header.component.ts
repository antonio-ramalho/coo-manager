import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LucideSearch, LucideBell, LucideUser } from '@lucide/angular';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [CommonModule, LucideSearch, LucideBell, LucideUser],
  templateUrl: './page-header.component.html',
})
export class PageHeaderComponent {
  usuarioLogado = {
    nome: 'Antonio Ramalho',
    cargo: 'Administrador',
    fotoUrl: 'assets/antonio.png',
  };
}
