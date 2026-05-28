import {Component} from '@angular/core';
import {CardUserComponent} from './components/card-user/card-user';
import {CardBoxComponent} from '../../shared/components/card-box/card-box';
import {LucideBell, LucideUser} from '@lucide/angular';

@Component({
  selector: 'app-home',
  imports: [
    CardUserComponent,
    CardBoxComponent,
    LucideBell,
    LucideUser
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
