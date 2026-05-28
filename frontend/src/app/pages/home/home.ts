import {Component} from '@angular/core';
import {CardUserComponent} from './components/card-user/card-user';
import {CardBoxComponent} from '../../shared/components/card-box/card-box';
import {LucideBell, LucideUser} from '@lucide/angular';
import {CardMenuComponent} from './components/card-menu/card-menu';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [
    CardUserComponent,
    CardBoxComponent,
    LucideBell,
    LucideUser,
    CardMenuComponent,
    RouterOutlet
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  usuarioLogado = {
    nome: 'Antonio Ramalho',
    cargo: 'Gerente Logístico',
    fotoUrl: 'assets/antonio.png'
  };
}
