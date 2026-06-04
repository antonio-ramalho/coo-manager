import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LucideBell, LucideUser, LucideSearch } from '@lucide/angular';
import { SideMenuComponent } from './components/side-menu/side-menu';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterOutlet, LucideBell, LucideUser, LucideSearch, SideMenuComponent],
  templateUrl: './home.html',
})
export class HomeComponent {
  usuarioLogado = {
    nome: 'Antonio Ramalho',
    cargo: 'Administrador',
    fotoUrl: 'assets/antonio.png',
  };
}
